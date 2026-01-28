package com.kafkaadmin.topic;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing producer state on a partition.
 *
 * @param producerId producer ID
 * @param producerEpoch producer epoch
 * @param lastSequence last sequence number
 * @param lastTimestamp last timestamp in ms
 * @param coordinatorEpoch coordinator epoch
 * @param currentTxnStartOffset current transaction start offset
 */
@Schema(description = "Producer state on a partition")
public record ProducerStateResponse(
        @Schema(description = "Producer ID", example = "12345")
        long producerId,

        @Schema(description = "Producer epoch", example = "1")
        int producerEpoch,

        @Schema(description = "Last sequence number", example = "100")
        int lastSequence,

        @Schema(description = "Last timestamp in milliseconds", example = "1704067200000")
        long lastTimestamp,

        @Schema(description = "Coordinator epoch (-1 if not transactional)", example = "-1")
        int coordinatorEpoch,

        @Schema(description = "Current transaction start offset (-1 if not in transaction)", example = "-1")
        long currentTxnStartOffset
) {
    public static ProducerStateResponse from(ProducerState state) {
        return new ProducerStateResponse(
                state.producerId(),
                state.producerEpoch(),
                state.lastSequence(),
                state.lastTimestamp(),
                state.coordinatorEpoch(),
                state.currentTxnStartOffset()
        );
    }
}
