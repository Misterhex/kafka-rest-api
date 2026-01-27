package com.kafkaadmin.topic;

import java.util.List;
import java.util.Map;

/**
 * Domain model representing a Kafka topic.
 *
 * @param name topic name
 * @param partitionCount number of partitions
 * @param replicationFactor number of replicas per partition
 * @param configs non-default topic configuration
 * @param partitions partition details
 * @param internal whether this is an internal Kafka topic
 */
public record Topic(
        String name,
        int partitionCount,
        int replicationFactor,
        Map<String, String> configs,
        List<TopicPartitionInfo> partitions,
        boolean internal
) {
}
