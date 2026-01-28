package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing metadata quorum information.
 *
 * @param leaderId current leader node ID
 * @param leaderEpoch current leader epoch
 * @param highWatermark high watermark offset
 * @param voters list of voter replicas
 * @param observers list of observer replicas
 */
@Schema(description = "Metadata quorum information (KRaft mode)")
public record QuorumInfoResponse(
        @Schema(description = "Current leader node ID", example = "1")
        int leaderId,

        @Schema(description = "Current leader epoch", example = "5")
        long leaderEpoch,

        @Schema(description = "High watermark offset", example = "12345")
        long highWatermark,

        @Schema(description = "Voter replicas")
        List<QuorumReplicaResponse> voters,

        @Schema(description = "Observer replicas")
        List<QuorumReplicaResponse> observers
) {
    public static QuorumInfoResponse from(QuorumInfo quorum) {
        return new QuorumInfoResponse(
                quorum.leaderId(),
                quorum.leaderEpoch(),
                quorum.highWatermark(),
                quorum.voters().stream().map(QuorumReplicaResponse::from).toList(),
                quorum.observers().stream().map(QuorumReplicaResponse::from).toList()
        );
    }
}
