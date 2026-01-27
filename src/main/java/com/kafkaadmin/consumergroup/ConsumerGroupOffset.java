package com.kafkaadmin.consumergroup;

public record ConsumerGroupOffset(
        String topic,
        int partition,
        long currentOffset,
        long endOffset,
        long lag
) {
}
