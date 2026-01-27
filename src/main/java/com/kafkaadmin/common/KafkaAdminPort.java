package com.kafkaadmin.common;

import com.kafkaadmin.cluster.Broker;
import com.kafkaadmin.cluster.ClusterInfo;
import com.kafkaadmin.consumergroup.ConsumerGroup;
import com.kafkaadmin.consumergroup.ConsumerGroupOffset;
import com.kafkaadmin.topic.Topic;
import com.kafkaadmin.topic.TopicPartitionInfo;

import java.util.List;
import java.util.Map;

public interface KafkaAdminPort {

    // Topic operations
    List<String> listTopicNames();

    Topic getTopic(String topicName);

    List<TopicPartitionInfo> getTopicPartitions(String topicName);

    // Consumer group operations
    List<String> listConsumerGroupIds();

    ConsumerGroup getConsumerGroup(String groupId);

    List<ConsumerGroupOffset> getConsumerGroupOffsets(String groupId);

    // Cluster operations
    ClusterInfo getClusterInfo();

    List<Broker> listBrokers();

    Broker getBroker(int brokerId);

    Map<String, String> getBrokerConfigs(int brokerId);
}
