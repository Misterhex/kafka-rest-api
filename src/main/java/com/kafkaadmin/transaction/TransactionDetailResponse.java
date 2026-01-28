package com.kafkaadmin.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing detailed transaction information.
 *
 * @param transactionalId transactional ID
 * @param coordinatorId coordinator broker ID
 * @param state transaction state
 * @param producerId producer ID
 * @param producerEpoch producer epoch
 * @param transactionTimeoutMs transaction timeout in ms
 * @param transactionStartTimeMs transaction start time in ms
 * @param topicPartitions topic-partitions involved
 */
@Schema(description = "Transaction details")
public record TransactionDetailResponse(
        @Schema(description = "Transactional ID", example = "my-transaction")
        String transactionalId,

        @Schema(description = "Coordinator broker ID", example = "1")
        int coordinatorId,

        @Schema(description = "Transaction state", example = "Ongoing")
        String state,

        @Schema(description = "Producer ID", example = "12345")
        long producerId,

        @Schema(description = "Producer epoch", example = "1")
        int producerEpoch,

        @Schema(description = "Transaction timeout in milliseconds", example = "60000")
        long transactionTimeoutMs,

        @Schema(description = "Transaction start time in milliseconds", example = "1704067200000")
        long transactionStartTimeMs,

        @Schema(description = "Topic-partitions involved in the transaction")
        List<TransactionTopicPartitionResponse> topicPartitions
) {
    public static TransactionDetailResponse from(TransactionDetail detail) {
        return new TransactionDetailResponse(
                detail.transactionalId(),
                detail.coordinatorId(),
                detail.state(),
                detail.producerId(),
                detail.producerEpoch(),
                detail.transactionTimeoutMs(),
                detail.transactionStartTimeMs(),
                detail.topicPartitions().stream()
                        .map(TransactionTopicPartitionResponse::from)
                        .toList()
        );
    }
}
