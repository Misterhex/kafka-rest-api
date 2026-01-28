package com.kafkaadmin.cluster;

import java.util.List;

/**
 * Domain model representing metadata quorum information (KRaft mode).
 *
 * @param leaderId current leader node ID
 * @param leaderEpoch current leader epoch
 * @param highWatermark high watermark offset
 * @param voters list of voter replicas
 * @param observers list of observer replicas
 */
public record QuorumInfo(
        int leaderId,
        long leaderEpoch,
        long highWatermark,
        List<QuorumReplica> voters,
        List<QuorumReplica> observers
) {
}
