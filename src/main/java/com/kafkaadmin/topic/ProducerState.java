package com.kafkaadmin.topic;

/**
 * Domain model representing the state of a producer on a partition.
 *
 * @param producerId producer ID
 * @param producerEpoch producer epoch
 * @param lastSequence last sequence number
 * @param lastTimestamp last timestamp in milliseconds
 * @param coordinatorEpoch coordinator epoch (-1 if not transactional)
 * @param currentTxnStartOffset current transaction start offset (-1 if not in transaction)
 */
public record ProducerState(
        long producerId,
        int producerEpoch,
        int lastSequence,
        long lastTimestamp,
        int coordinatorEpoch,
        long currentTxnStartOffset
) {
}
