package com.kafkaadmin.cluster;

import java.util.List;

/**
 * Domain model representing log directory information for a broker.
 *
 * @param path log directory path
 * @param error error message if directory is offline, null otherwise
 * @param totalBytes total bytes in the directory (-1 if unknown)
 * @param usableBytes usable bytes in the directory (-1 if unknown)
 * @param partitions list of partition replicas in this directory
 */
public record LogDirInfo(
        String path,
        String error,
        long totalBytes,
        long usableBytes,
        List<LogDirPartition> partitions
) {
}
