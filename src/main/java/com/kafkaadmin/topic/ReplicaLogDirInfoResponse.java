package com.kafkaadmin.topic;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing replica log directory information.
 *
 * @param brokerId broker ID
 * @param partition partition number
 * @param currentLogDir current log directory path
 * @param futureLogDir future log directory path
 * @param currentReplicaSize current replica size in bytes
 * @param currentReplicaOffsetLag current replica offset lag
 * @param futureReplicaSize future replica size in bytes
 * @param futureReplicaOffsetLag future replica offset lag
 */
@Schema(description = "Replica log directory information")
public record ReplicaLogDirInfoResponse(
        @Schema(description = "Broker ID", example = "1")
        int brokerId,

        @Schema(description = "Partition number", example = "0")
        int partition,

        @Schema(description = "Current log directory path", example = "/var/kafka-logs")
        String currentLogDir,

        @Schema(description = "Future log directory path (null if not moving)")
        String futureLogDir,

        @Schema(description = "Current replica size in bytes", example = "1048576")
        long currentReplicaSize,

        @Schema(description = "Current replica offset lag", example = "0")
        long currentReplicaOffsetLag,

        @Schema(description = "Future replica size in bytes (-1 if not moving)", example = "-1")
        long futureReplicaSize,

        @Schema(description = "Future replica offset lag (-1 if not moving)", example = "-1")
        long futureReplicaOffsetLag
) {
    public static ReplicaLogDirInfoResponse from(ReplicaLogDirInfo info) {
        return new ReplicaLogDirInfoResponse(
                info.brokerId(),
                info.partition(),
                info.currentLogDir(),
                info.futureLogDir(),
                info.currentReplicaSize(),
                info.currentReplicaOffsetLag(),
                info.futureReplicaSize(),
                info.futureReplicaOffsetLag()
        );
    }
}
