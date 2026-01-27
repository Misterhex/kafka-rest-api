package com.kafkaadmin.common;

import com.kafkaadmin.cluster.Broker;
import com.kafkaadmin.cluster.ClusterInfo;
import com.kafkaadmin.consumergroup.ConsumerGroup;
import com.kafkaadmin.consumergroup.ConsumerGroupOffset;
import com.kafkaadmin.topic.Topic;
import com.kafkaadmin.topic.TopicPartitionInfo;

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
}
