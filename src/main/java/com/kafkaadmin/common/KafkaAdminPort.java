package com.kafkaadmin.common;

import com.kafkaadmin.acl.Acl;
import com.kafkaadmin.cluster.*;
import com.kafkaadmin.consumergroup.ConsumerGroup;
import com.kafkaadmin.consumergroup.ConsumerGroupOffset;
import com.kafkaadmin.quota.ClientQuota;
import com.kafkaadmin.token.DelegationToken;
import com.kafkaadmin.topic.ProducerState;
import com.kafkaadmin.topic.ReplicaLogDirInfo;
import com.kafkaadmin.topic.Topic;
import com.kafkaadmin.topic.TopicPartitionInfo;
import com.kafkaadmin.transaction.TransactionDetail;
import com.kafkaadmin.transaction.TransactionListing;
import com.kafkaadmin.user.UserScramCredential;

import java.util.List;
import java.util.Map;

/**
 * Port interface defining Kafka administration operations.
 *
 * <p>Abstracts Kafka AdminClient interactions to enable testability
 * and decouple business logic from infrastructure concerns.
 */
public interface KafkaAdminPort {

    // Topic operations

    /**
     * Lists all topic names in the Kafka cluster.
     *
     * @return list of topic names
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<String> listTopicNames();

    /**
     * Retrieves detailed information about a topic.
     *
     * @param topicName the name of the topic
     * @return topic details including partitions and configuration
     * @throws com.kafkaadmin.topic.TopicNotFoundException if the topic does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    Topic getTopic(String topicName);

    /**
     * Retrieves partition information for a topic.
     *
     * @param topicName the name of the topic
     * @return list of partition information
     * @throws com.kafkaadmin.topic.TopicNotFoundException if the topic does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<TopicPartitionInfo> getTopicPartitions(String topicName);

    /**
     * Describes producers on a specific topic partition.
     *
     * @param topicName the name of the topic
     * @param partition the partition number
     * @return list of producer states
     * @throws com.kafkaadmin.topic.TopicNotFoundException if the topic does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<ProducerState> describeProducers(String topicName, int partition);

    /**
     * Describes replica log directories for a topic.
     *
     * @param topicName the name of the topic
     * @return list of replica log directory information
     * @throws com.kafkaadmin.topic.TopicNotFoundException if the topic does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<ReplicaLogDirInfo> describeReplicaLogDirs(String topicName);

    // Consumer group operations

    /**
     * Lists all consumer group IDs in the Kafka cluster.
     *
     * @return list of consumer group IDs
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<String> listConsumerGroupIds();

    /**
     * Retrieves detailed information about a consumer group.
     *
     * @param groupId the consumer group ID
     * @return consumer group details including members
     * @throws com.kafkaadmin.consumergroup.ConsumerGroupNotFoundException if the group does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    ConsumerGroup getConsumerGroup(String groupId);

    /**
     * Retrieves offset information for a consumer group.
     *
     * @param groupId the consumer group ID
     * @return list of offset information per topic-partition
     * @throws com.kafkaadmin.consumergroup.ConsumerGroupNotFoundException if the group does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<ConsumerGroupOffset> getConsumerGroupOffsets(String groupId);

    // Cluster operations

    /**
     * Retrieves cluster information including controller and brokers.
     *
     * @return cluster information
     * @throws KafkaAdminException if communication with Kafka fails
     */
    ClusterInfo getClusterInfo();

    /**
     * Lists all brokers in the Kafka cluster.
     *
     * @return list of brokers
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<Broker> listBrokers();

    /**
     * Retrieves information about a specific broker.
     *
     * @param brokerId the broker ID
     * @return broker information
     * @throws com.kafkaadmin.cluster.BrokerNotFoundException if the broker does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    Broker getBroker(int brokerId);

    /**
     * Retrieves non-default configuration for a broker.
     *
     * @param brokerId the broker ID
     * @return map of configuration key-value pairs
     * @throws com.kafkaadmin.cluster.BrokerNotFoundException if the broker does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    Map<String, String> getBrokerConfigs(int brokerId);

    /**
     * Describes log directories for a broker.
     *
     * @param brokerId the broker ID
     * @return list of log directory information
     * @throws com.kafkaadmin.cluster.BrokerNotFoundException if the broker does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<LogDirInfo> describeLogDirs(int brokerId);

    /**
     * Lists ongoing partition reassignments.
     *
     * @return list of partition reassignments
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<PartitionReassignment> listPartitionReassignments();

    /**
     * Describes Kafka features.
     *
     * @return list of Kafka features
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<KafkaFeature> describeFeatures();

    /**
     * Describes the metadata quorum (KRaft mode).
     *
     * @return quorum information
     * @throws KafkaAdminException if communication with Kafka fails or cluster is not in KRaft mode
     */
    QuorumInfo describeMetadataQuorum();

    // ACL operations

    /**
     * Describes all ACLs in the cluster.
     *
     * @return list of ACL entries
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<Acl> describeAcls();

    // Transaction operations

    /**
     * Lists all transactions in the cluster.
     *
     * @return list of transaction listings
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<TransactionListing> listTransactions();

    /**
     * Describes a specific transaction.
     *
     * @param transactionalId the transactional ID
     * @return transaction details
     * @throws com.kafkaadmin.transaction.TransactionNotFoundException if the transaction does not exist
     * @throws KafkaAdminException if communication with Kafka fails
     */
    TransactionDetail describeTransaction(String transactionalId);

    // Quota operations

    /**
     * Describes all client quotas.
     *
     * @return list of client quota configurations
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<ClientQuota> describeClientQuotas();

    // Delegation token operations

    /**
     * Describes all delegation tokens.
     *
     * @return list of delegation tokens
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<DelegationToken> describeDelegationTokens();

    // User SCRAM credential operations

    /**
     * Describes SCRAM credentials for all users.
     *
     * @return list of user SCRAM credentials
     * @throws KafkaAdminException if communication with Kafka fails
     */
    List<UserScramCredential> describeUserScramCredentials();
}
