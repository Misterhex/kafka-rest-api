package com.kafkaadmin.common;

import com.kafkaadmin.cluster.Broker;
import com.kafkaadmin.cluster.BrokerNotFoundException;
import com.kafkaadmin.cluster.ClusterInfo;
import com.kafkaadmin.consumergroup.ConsumerGroup;
import com.kafkaadmin.consumergroup.ConsumerGroupMember;
import com.kafkaadmin.consumergroup.ConsumerGroupNotFoundException;
import com.kafkaadmin.consumergroup.ConsumerGroupOffset;
import com.kafkaadmin.topic.Topic;
import com.kafkaadmin.topic.TopicNotFoundException;
import com.kafkaadmin.topic.TopicPartitionInfo;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
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
}
