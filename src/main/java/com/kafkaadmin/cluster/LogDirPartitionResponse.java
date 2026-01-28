package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing a partition replica in a log directory.
 *
 * @param topic topic name
 * @param partition partition number
 * @param size size in bytes
 * @param offsetLag lag behind leader
 * @param isFuture whether this is a future replica
 */
@Schema(description = "Partition replica in log directory")
public record LogDirPartitionResponse(
        @Schema(description = "Topic name", example = "my-topic")
        String topic,

        @Schema(description = "Partition number", example = "0")
        int partition,

        @Schema(description = "Size in bytes", example = "1048576")
        long size,

        @Schema(description = "Offset lag behind leader", example = "0")
        long offsetLag,

        @Schema(description = "Whether this is a future replica for reassignment", example = "false")
        boolean isFuture
) {
    public static LogDirPartitionResponse from(LogDirPartition partition) {
        return new LogDirPartitionResponse(
                partition.topic(),
                partition.partition(),
                partition.size(),
                partition.offsetLag(),
                partition.isFuture()
        );
    }
}
