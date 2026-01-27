package com.kafkaadmin.topic;

import java.util.List;

public record TopicPartitionInfo(
        int partition,
        int leader,
        List<Integer> replicas,
        List<Integer> isr
) {
}
