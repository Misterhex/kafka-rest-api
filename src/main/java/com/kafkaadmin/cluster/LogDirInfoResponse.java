package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing log directory information for API responses.
 *
 * @param path log directory path
 * @param error error message if directory is offline
 * @param totalBytes total bytes in the directory
 * @param usableBytes usable bytes in the directory
 * @param partitions list of partition replicas in this directory
 */
@Schema(description = "Log directory information")
public record LogDirInfoResponse(
        @Schema(description = "Log directory path", example = "/var/kafka-logs")
        String path,

        @Schema(description = "Error if directory is offline", example = "null")
        String error,

        @Schema(description = "Total bytes in directory", example = "107374182400")
        long totalBytes,

        @Schema(description = "Usable bytes in directory", example = "53687091200")
        long usableBytes,

        @Schema(description = "Partition replicas in this directory")
        List<LogDirPartitionResponse> partitions
) {
    public static LogDirInfoResponse from(LogDirInfo logDir) {
        return new LogDirInfoResponse(
                logDir.path(),
                logDir.error(),
                logDir.totalBytes(),
                logDir.usableBytes(),
                logDir.partitions().stream()
                        .map(LogDirPartitionResponse::from)
                        .toList()
        );
    }
}
