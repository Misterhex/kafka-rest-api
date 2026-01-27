package com.kafkaadmin.consumergroup;

/**
 * Domain model representing offset information for a consumer group partition.
 *
 * @param topic topic name
 * @param partition partition number
 * @param currentOffset last committed offset
 * @param endOffset latest offset in the partition
 * @param lag difference between end offset and current offset
 */
public record ConsumerGroupOffset(
        String topic,
        int partition,
        long currentOffset,
        long endOffset,
        long lag
) {
}
