package com.kafkaadmin.cluster;

import java.util.List;

/**
 * Domain model representing an ongoing partition reassignment.
 *
 * @param topic topic name
 * @param partition partition number
 * @param replicas current replica broker IDs
 * @param addingReplicas broker IDs being added
 * @param removingReplicas broker IDs being removed
 */
public record PartitionReassignment(
        String topic,
        int partition,
        List<Integer> replicas,
        List<Integer> addingReplicas,
        List<Integer> removingReplicas
) {
}
