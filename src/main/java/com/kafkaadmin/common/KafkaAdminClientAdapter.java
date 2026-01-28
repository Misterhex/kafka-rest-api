package com.kafkaadmin.common;

import com.kafkaadmin.acl.Acl;
import com.kafkaadmin.cluster.Broker;
import com.kafkaadmin.cluster.BrokerNotFoundException;
import com.kafkaadmin.cluster.ClusterInfo;
import com.kafkaadmin.cluster.KafkaFeature;
import com.kafkaadmin.cluster.LogDirInfo;
import com.kafkaadmin.cluster.LogDirPartition;
import com.kafkaadmin.consumergroup.ConsumerGroup;
import com.kafkaadmin.consumergroup.ConsumerGroupMember;
import com.kafkaadmin.consumergroup.ConsumerGroupNotFoundException;
import com.kafkaadmin.consumergroup.ConsumerGroupOffset;
import com.kafkaadmin.quota.ClientQuota;
import com.kafkaadmin.sharegroup.ShareGroup;
import com.kafkaadmin.sharegroup.ShareGroupMember;
import com.kafkaadmin.sharegroup.ShareGroupNotFoundException;
import com.kafkaadmin.token.DelegationToken;
import com.kafkaadmin.topic.ProducerState;
import com.kafkaadmin.topic.ReplicaLogDirInfo;
import com.kafkaadmin.topic.Topic;
import com.kafkaadmin.topic.TopicNotFoundException;
import com.kafkaadmin.topic.TopicPartitionInfo;
import com.kafkaadmin.transaction.TransactionDetail;
import com.kafkaadmin.transaction.TransactionListing;
import com.kafkaadmin.transaction.TransactionNotFoundException;
import com.kafkaadmin.transaction.TransactionTopicPartition;
import com.kafkaadmin.user.ScramCredentialInfo;
import com.kafkaadmin.user.UserScramCredential;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionReplica;
import org.apache.kafka.common.acl.AclBinding;
import org.apache.kafka.common.acl.AclBindingFilter;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.quota.ClientQuotaEntity;
import org.apache.kafka.common.quota.ClientQuotaFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Adapter implementing {@link KafkaAdminPort} using Kafka's AdminClient.
 *
 * <p>Translates Kafka AdminClient operations into domain model objects
 * and handles exceptions appropriately.
 */
@Component
class KafkaAdminClientAdapter implements KafkaAdminPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaAdminClientAdapter.class);

    private final AdminClient adminClient;

    /**
     * Creates an adapter with the given AdminClient.
     *
     * @param adminClient the Kafka AdminClient to use
     */
    KafkaAdminClientAdapter(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> listTopicNames() {
        try {
            return new ArrayList<>(adminClient.listTopics().names().get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while listing topics", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to list topics", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public Topic getTopic(String topicName) {
        try {
            DescribeTopicsResult result = adminClient.describeTopics(List.of(topicName));
            TopicDescription description = result.topicNameValues().get(topicName).get();

            Map<String, String> configs = getTopicConfigs(topicName);

            List<TopicPartitionInfo> partitions = description.partitions().stream()
                    .map(p -> new TopicPartitionInfo(
                            p.partition(),
                            p.leader() != null ? p.leader().id() : -1,
                            p.replicas().stream().map(Node::id).toList(),
                            p.isr().stream().map(Node::id).toList()))
                    .toList();

            return new Topic(
                    topicName,
                    description.partitions().size(),
                    description.partitions().isEmpty() ? 0 :
                            description.partitions().get(0).replicas().size(),
                    configs,
                    partitions,
                    description.isInternal());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing topic: " + topicName, e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof org.apache.kafka.common.errors.UnknownTopicOrPartitionException) {
                throw new TopicNotFoundException(topicName);
            }
            throw new KafkaAdminException("Failed to describe topic: " + topicName, e.getCause());
        }
    }

    private Map<String, String> getTopicConfigs(String topicName) {
        try {
            ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            DescribeConfigsResult configsResult = adminClient.describeConfigs(List.of(resource));
            Config config = configsResult.all().get().get(resource);

            return config.entries().stream()
                    .filter(entry -> !entry.isDefault())
                    .collect(Collectors.toMap(
                            ConfigEntry::name,
                            ConfigEntry::value));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while getting topic configs: " + topicName, e);
        } catch (ExecutionException e) {
            log.warn("Failed to get configs for topic {}: {}", topicName, e.getMessage());
            return Collections.emptyMap();
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<TopicPartitionInfo> getTopicPartitions(String topicName) {
        Topic topic = getTopic(topicName);
        return topic.partitions();
    }

    /** {@inheritDoc} */
    @Override
    public List<ProducerState> describeProducers(String topicName, int partition) {
        // First verify the topic exists
        getTopic(topicName);

        try {
            TopicPartition tp = new TopicPartition(topicName, partition);
            DescribeProducersResult result = adminClient.describeProducers(List.of(tp));
            DescribeProducersResult.PartitionProducerState partitionState = result.partitionResult(tp).get();

            return partitionState.activeProducers().stream()
                    .map(p -> new ProducerState(
                            p.producerId(),
                            p.producerEpoch(),
                            p.lastSequence(),
                            p.lastTimestamp(),
                            p.coordinatorEpoch().orElse(-1),
                            p.currentTransactionStartOffset().orElse(-1L)))
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing producers: " + topicName + "-" + partition, e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to describe producers: " + topicName + "-" + partition, e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<ReplicaLogDirInfo> describeReplicaLogDirs(String topicName) {
        Topic topic = getTopic(topicName);

        try {
            // Get all replicas for this topic
            List<TopicPartitionReplica> replicas = new ArrayList<>();
            for (TopicPartitionInfo partition : topic.partitions()) {
                for (int brokerId : partition.replicas()) {
                    replicas.add(new TopicPartitionReplica(topicName, partition.partition(), brokerId));
                }
            }

            if (replicas.isEmpty()) {
                return Collections.emptyList();
            }

            DescribeReplicaLogDirsResult result = adminClient.describeReplicaLogDirs(replicas);
            Map<TopicPartitionReplica, DescribeReplicaLogDirsResult.ReplicaLogDirInfo> logDirInfos = result.all().get();

            return logDirInfos.entrySet().stream()
                    .map(entry -> {
                        TopicPartitionReplica replica = entry.getKey();
                        DescribeReplicaLogDirsResult.ReplicaLogDirInfo info = entry.getValue();
                        return new ReplicaLogDirInfo(
                                replica.brokerId(),
                                replica.partition(),
                                info.getCurrentReplicaLogDir(),
                                info.getFutureReplicaLogDir(),
                                info.getCurrentReplicaOffsetLag() >= 0 ? 0 : -1,
                                info.getCurrentReplicaOffsetLag(),
                                info.getFutureReplicaOffsetLag() >= 0 ? 0 : -1,
                                info.getFutureReplicaOffsetLag());
                    })
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing replica log dirs: " + topicName, e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to describe replica log dirs: " + topicName, e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("removal")
    public List<String> listConsumerGroupIds() {
        try {
            return adminClient.listConsumerGroups().all().get().stream()
                    .map(ConsumerGroupListing::groupId)
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while listing consumer groups", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to list consumer groups", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public ConsumerGroup getConsumerGroup(String groupId) {
        try {
            DescribeConsumerGroupsResult result = adminClient.describeConsumerGroups(List.of(groupId));
            ConsumerGroupDescription description = result.describedGroups().get(groupId).get();

            List<ConsumerGroupMember> members = description.members().stream()
                    .map(m -> new ConsumerGroupMember(
                            m.consumerId(),
                            m.clientId(),
                            m.host(),
                            m.assignment().topicPartitions().stream()
                                    .map(tp -> tp.topic() + "-" + tp.partition())
                                    .toList()))
                    .toList();

            return new ConsumerGroup(
                    groupId,
                    description.state().toString(),
                    description.partitionAssignor(),
                    description.coordinator() != null ? description.coordinator().id() : -1,
                    members);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing consumer group: " + groupId, e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof org.apache.kafka.common.errors.GroupIdNotFoundException) {
                throw new ConsumerGroupNotFoundException(groupId);
            }
            throw new KafkaAdminException("Failed to describe consumer group: " + groupId, e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<ConsumerGroupOffset> getConsumerGroupOffsets(String groupId) {
        try {
            // First verify the group exists
            getConsumerGroup(groupId);

            ListConsumerGroupOffsetsResult offsetsResult =
                    adminClient.listConsumerGroupOffsets(groupId);
            Map<TopicPartition, OffsetAndMetadata> offsets =
                    offsetsResult.partitionsToOffsetAndMetadata().get();

            if (offsets.isEmpty()) {
                return Collections.emptyList();
            }

            // Get end offsets for lag calculation
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> endOffsets =
                    adminClient.listOffsets(
                            offsets.keySet().stream()
                                    .collect(Collectors.toMap(
                                            tp -> tp,
                                            tp -> OffsetSpec.latest()))).all().get();

            return offsets.entrySet().stream()
                    .map(entry -> {
                        TopicPartition tp = entry.getKey();
                        long currentOffset = entry.getValue().offset();
                        long endOffset = endOffsets.get(tp).offset();
                        long lag = Math.max(0, endOffset - currentOffset);

                        return new ConsumerGroupOffset(
                                tp.topic(),
                                tp.partition(),
                                currentOffset,
                                endOffset,
                                lag);
                    })
                    .toList();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while getting consumer group offsets: " + groupId, e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to get consumer group offsets: " + groupId, e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public ClusterInfo getClusterInfo() {
        try {
            DescribeClusterResult result = adminClient.describeCluster();
            String clusterId = result.clusterId().get();
            Node controller = result.controller().get();
            Collection<Node> nodes = result.nodes().get();

            List<Broker> brokers = nodes.stream()
                    .map(node -> new Broker(
                            node.id(),
                            node.host(),
                            node.port(),
                            node.rack(),
                            node.id() == controller.id()))
                    .toList();

            return new ClusterInfo(
                    clusterId,
                    controller.id(),
                    brokers);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while getting cluster info", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to get cluster info", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Broker> listBrokers() {
        return getClusterInfo().brokers();
    }

    /** {@inheritDoc} */
    @Override
    public Broker getBroker(int brokerId) {
        return listBrokers().stream()
                .filter(b -> b.id() == brokerId)
                .findFirst()
                .orElseThrow(() -> new BrokerNotFoundException(brokerId));
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> getBrokerConfigs(int brokerId) {
        // First verify broker exists
        getBroker(brokerId);

        try {
            ConfigResource resource = new ConfigResource(ConfigResource.Type.BROKER, String.valueOf(brokerId));
            DescribeConfigsResult configsResult = adminClient.describeConfigs(List.of(resource));
            Config config = configsResult.all().get().get(resource);

            return config.entries().stream()
                    .filter(entry -> !entry.isDefault() && !entry.isSensitive())
                    .collect(Collectors.toMap(
                            ConfigEntry::name,
                            ConfigEntry::value));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while getting broker configs: " + brokerId, e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to get broker configs: " + brokerId, e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<LogDirInfo> describeLogDirs(int brokerId) {
        // First verify broker exists
        getBroker(brokerId);

        try {
            DescribeLogDirsResult result = adminClient.describeLogDirs(List.of(brokerId));
            Map<Integer, Map<String, LogDirDescription>> logDirs = result.allDescriptions().get();
            Map<String, LogDirDescription> brokerLogDirs = logDirs.get(brokerId);

            if (brokerLogDirs == null) {
                return Collections.emptyList();
            }

            return brokerLogDirs.entrySet().stream()
                    .map(entry -> {
                        String path = entry.getKey();
                        LogDirDescription desc = entry.getValue();

                        String error = desc.error() != null ? desc.error().getMessage() : null;
                        long totalBytes = desc.totalBytes().orElse(-1L);
                        long usableBytes = desc.usableBytes().orElse(-1L);

                        List<LogDirPartition> partitions = desc.replicaInfos().entrySet().stream()
                                .map(replicaEntry -> {
                                    TopicPartition tp = replicaEntry.getKey();
                                    ReplicaInfo replicaInfo = replicaEntry.getValue();
                                    return new LogDirPartition(
                                            tp.topic(),
                                            tp.partition(),
                                            replicaInfo.size(),
                                            replicaInfo.offsetLag(),
                                            replicaInfo.isFuture());
                                })
                                .toList();

                        return new LogDirInfo(path, error, totalBytes, usableBytes, partitions);
                    })
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing log dirs: " + brokerId, e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to describe log dirs: " + brokerId, e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<com.kafkaadmin.cluster.PartitionReassignment> listPartitionReassignments() {
        try {
            ListPartitionReassignmentsResult result = adminClient.listPartitionReassignments();
            Map<TopicPartition, org.apache.kafka.clients.admin.PartitionReassignment> reassignments = result.reassignments().get();

            return reassignments.entrySet().stream()
                    .map(entry -> {
                        TopicPartition tp = entry.getKey();
                        org.apache.kafka.clients.admin.PartitionReassignment reassignment = entry.getValue();
                        return new com.kafkaadmin.cluster.PartitionReassignment(
                                tp.topic(),
                                tp.partition(),
                                reassignment.replicas(),
                                reassignment.addingReplicas(),
                                reassignment.removingReplicas());
                    })
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while listing partition reassignments", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to list partition reassignments", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<KafkaFeature> describeFeatures() {
        try {
            DescribeFeaturesResult result = adminClient.describeFeatures();
            FeatureMetadata featureMetadata = result.featureMetadata().get();

            List<KafkaFeature> features = new ArrayList<>();

            // Add supported features
            featureMetadata.supportedFeatures().forEach((name, versionRange) -> {
                short finalizedVersion = 0;
                if (featureMetadata.finalizedFeatures().containsKey(name)) {
                    finalizedVersion = featureMetadata.finalizedFeatures().get(name).maxVersionLevel();
                }
                features.add(new KafkaFeature(
                        name,
                        versionRange.minVersion(),
                        versionRange.maxVersion(),
                        finalizedVersion));
            });

            return features;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing features", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to describe features", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public com.kafkaadmin.cluster.QuorumInfo describeMetadataQuorum() {
        try {
            DescribeMetadataQuorumResult result = adminClient.describeMetadataQuorum();
            org.apache.kafka.clients.admin.QuorumInfo quorumInfo = result.quorumInfo().get();

            List<com.kafkaadmin.cluster.QuorumReplica> voters = quorumInfo.voters().stream()
                    .map(r -> new com.kafkaadmin.cluster.QuorumReplica(
                            r.replicaId(),
                            r.logEndOffset(),
                            r.lastFetchTimestamp().orElse(-1L),
                            r.lastCaughtUpTimestamp().orElse(-1L)))
                    .toList();

            List<com.kafkaadmin.cluster.QuorumReplica> observers = quorumInfo.observers().stream()
                    .map(r -> new com.kafkaadmin.cluster.QuorumReplica(
                            r.replicaId(),
                            r.logEndOffset(),
                            r.lastFetchTimestamp().orElse(-1L),
                            r.lastCaughtUpTimestamp().orElse(-1L)))
                    .toList();

            return new com.kafkaadmin.cluster.QuorumInfo(
                    quorumInfo.leaderId(),
                    quorumInfo.leaderEpoch(),
                    quorumInfo.highWatermark(),
                    voters,
                    observers);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing metadata quorum", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to describe metadata quorum", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Acl> describeAcls() {
        try {
            DescribeAclsResult result = adminClient.describeAcls(AclBindingFilter.ANY);
            Collection<AclBinding> bindings = result.values().get();

            return bindings.stream()
                    .map(binding -> new Acl(
                            binding.pattern().resourceType().name(),
                            binding.pattern().name(),
                            binding.pattern().patternType().name(),
                            binding.entry().principal(),
                            binding.entry().host(),
                            binding.entry().operation().name(),
                            binding.entry().permissionType().name()))
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing ACLs", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to describe ACLs", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<TransactionListing> listTransactions() {
        try {
            ListTransactionsResult result = adminClient.listTransactions();
            Collection<org.apache.kafka.clients.admin.TransactionListing> listings = result.all().get();

            return listings.stream()
                    .map(l -> new TransactionListing(
                            l.transactionalId(),
                            l.producerId(),
                            l.state().toString()))
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while listing transactions", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to list transactions", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public TransactionDetail describeTransaction(String transactionalId) {
        try {
            DescribeTransactionsResult result = adminClient.describeTransactions(List.of(transactionalId));
            TransactionDescription description = result.description(transactionalId).get();

            List<TransactionTopicPartition> topicPartitions = description.topicPartitions().stream()
                    .map(tp -> new TransactionTopicPartition(tp.topic(), tp.partition()))
                    .toList();

            return new TransactionDetail(
                    transactionalId,
                    description.coordinatorId(),
                    description.state().toString(),
                    description.producerId(),
                    description.producerEpoch(),
                    description.transactionTimeoutMs(),
                    description.transactionStartTimeMs().orElse(-1L),
                    topicPartitions);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing transaction: " + transactionalId, e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof org.apache.kafka.common.errors.TransactionalIdNotFoundException) {
                throw new TransactionNotFoundException(transactionalId);
            }
            throw new KafkaAdminException("Failed to describe transaction: " + transactionalId, e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<ClientQuota> describeClientQuotas() {
        try {
            DescribeClientQuotasResult result = adminClient.describeClientQuotas(ClientQuotaFilter.all());
            Map<ClientQuotaEntity, Map<String, Double>> quotas = result.entities().get();

            return quotas.entrySet().stream()
                    .flatMap(entry -> {
                        ClientQuotaEntity entity = entry.getKey();
                        Map<String, Double> quotaValues = entry.getValue();

                        // Each entity can have multiple entity types
                        return entity.entries().entrySet().stream()
                                .map(entityEntry -> new ClientQuota(
                                        entityEntry.getKey(),
                                        entityEntry.getValue(),
                                        quotaValues));
                    })
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing client quotas", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to describe client quotas", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<DelegationToken> describeDelegationTokens() {
        try {
            DescribeDelegationTokenResult result = adminClient.describeDelegationToken();
            List<org.apache.kafka.common.security.token.delegation.DelegationToken> tokens = result.delegationTokens().get();

            return tokens.stream()
                    .map(token -> {
                        org.apache.kafka.common.security.token.delegation.TokenInformation info = token.tokenInfo();
                        return new DelegationToken(
                                info.tokenId(),
                                info.owner().toString(),
                                info.tokenRequester().toString(),
                                info.renewers().stream().map(Object::toString).toList(),
                                info.issueTimestamp(),
                                info.expiryTimestamp(),
                                info.maxTimestamp());
                    })
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing delegation tokens", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to describe delegation tokens", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<String> listShareGroupIds() {
        try {
            ListGroupsOptions options = new ListGroupsOptions()
                    .withTypes(Set.of(org.apache.kafka.common.GroupType.SHARE));
            return adminClient.listGroups(options).all().get().stream()
                    .map(GroupListing::groupId)
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while listing share groups", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to list share groups", e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public ShareGroup describeShareGroup(String groupId) {
        try {
            DescribeShareGroupsResult result = adminClient.describeShareGroups(List.of(groupId));
            ShareGroupDescription description = result.describedGroups().get(groupId).get();

            List<ShareGroupMember> members = description.members().stream()
                    .map(m -> new ShareGroupMember(
                            m.consumerId(),
                            m.clientId(),
                            m.host(),
                            m.assignment().topicPartitions().stream()
                                    .map(tp -> tp.topic() + "-" + tp.partition())
                                    .toList()))
                    .toList();

            return new ShareGroup(
                    groupId,
                    description.groupState().toString(),
                    description.coordinator() != null ? description.coordinator().id() : -1,
                    members);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing share group: " + groupId, e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof org.apache.kafka.common.errors.GroupIdNotFoundException) {
                throw new ShareGroupNotFoundException(groupId);
            }
            throw new KafkaAdminException("Failed to describe share group: " + groupId, e.getCause());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<UserScramCredential> describeUserScramCredentials() {
        try {
            DescribeUserScramCredentialsResult result = adminClient.describeUserScramCredentials();
            Map<String, UserScramCredentialsDescription> credentials = result.all().get();

            return credentials.entrySet().stream()
                    .map(entry -> {
                        String userName = entry.getKey();
                        UserScramCredentialsDescription desc = entry.getValue();

                        List<ScramCredentialInfo> credentialInfos = desc.credentialInfos().stream()
                                .map(info -> new ScramCredentialInfo(
                                        info.mechanism().mechanismName(),
                                        info.iterations()))
                                .toList();

                        return new UserScramCredential(userName, credentialInfos);
                    })
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaAdminException("Interrupted while describing user SCRAM credentials", e);
        } catch (ExecutionException e) {
            throw new KafkaAdminException("Failed to describe user SCRAM credentials", e.getCause());
        }
    }
}
