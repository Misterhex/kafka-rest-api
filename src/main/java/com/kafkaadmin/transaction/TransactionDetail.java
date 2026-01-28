package com.kafkaadmin.transaction;

import java.util.List;

/**
 * Domain model representing detailed transaction information.
 *
 * @param transactionalId transactional ID
 * @param coordinatorId coordinator broker ID
 * @param state transaction state
 * @param producerId producer ID
 * @param producerEpoch producer epoch
 * @param transactionTimeoutMs transaction timeout in milliseconds
 * @param transactionStartTimeMs transaction start time in milliseconds
 * @param topicPartitions topic-partitions involved in the transaction
 */
public record TransactionDetail(
        String transactionalId,
        int coordinatorId,
        String state,
        long producerId,
        int producerEpoch,
        long transactionTimeoutMs,
        long transactionStartTimeMs,
        List<TransactionTopicPartition> topicPartitions
) {
}
