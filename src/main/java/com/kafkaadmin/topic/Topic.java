package com.kafkaadmin.topic;

import java.util.List;
import java.util.Map;

public record Topic(
        String name,
        int partitionCount,
        int replicationFactor,
        Map<String, String> configs,
        List<TopicPartitionInfo> partitions,
        boolean internal
) {
}
