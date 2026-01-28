package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing a replica in the metadata quorum.
 *
 * @param replicaId replica node ID
 * @param logEndOffset log end offset
 * @param lastFetchTimestamp last fetch timestamp in ms
 * @param lastCaughtUpTimestamp last caught up timestamp in ms
 */
@Schema(description = "Quorum replica information")
public record QuorumReplicaResponse(
        @Schema(description = "Replica node ID", example = "1")
        int replicaId,

        @Schema(description = "Log end offset", example = "12345")
        long logEndOffset,

        @Schema(description = "Last fetch timestamp in milliseconds", example = "1704067200000")
        long lastFetchTimestamp,

        @Schema(description = "Last caught up timestamp in milliseconds", example = "1704067200000")
        long lastCaughtUpTimestamp
) {
    public static QuorumReplicaResponse from(QuorumReplica replica) {
        return new QuorumReplicaResponse(
                replica.replicaId(),
                replica.logEndOffset(),
                replica.lastFetchTimestamp(),
                replica.lastCaughtUpTimestamp()
        );
    }
}
