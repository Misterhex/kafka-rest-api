package com.kafkaadmin.cluster;

/**
 * Domain model representing a partition replica within a log directory.
 *
 * @param topic topic name
 * @param partition partition number
 * @param size size of the replica in bytes
 * @param offsetLag lag of the replica behind the leader
 * @param isFuture whether this is a future replica for a reassignment
 */
public record LogDirPartition(
        String topic,
        int partition,
        long size,
        long offsetLag,
        boolean isFuture
) {
}
