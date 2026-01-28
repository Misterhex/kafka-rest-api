package com.kafkaadmin.cluster;

/**
 * Domain model representing a replica in the metadata quorum.
 *
 * @param replicaId replica node ID
 * @param logEndOffset log end offset of the replica
 * @param lastFetchTimestamp last fetch timestamp in milliseconds
 * @param lastCaughtUpTimestamp last caught up timestamp in milliseconds
 */
public record QuorumReplica(
        int replicaId,
        long logEndOffset,
        long lastFetchTimestamp,
        long lastCaughtUpTimestamp
) {
}
