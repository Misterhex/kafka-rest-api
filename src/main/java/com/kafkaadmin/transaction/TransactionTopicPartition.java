package com.kafkaadmin.transaction;

/**
 * Domain model representing a topic-partition in a transaction.
 *
 * @param topic topic name
 * @param partition partition number
 */
public record TransactionTopicPartition(
        String topic,
        int partition
) {
}
