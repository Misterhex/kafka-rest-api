package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing a partition reassignment in progress.
 *
 * @param topic topic name
 * @param partition partition number
 * @param replicas current replica broker IDs
 * @param addingReplicas broker IDs being added
 * @param removingReplicas broker IDs being removed
 */
@Schema(description = "Partition reassignment in progress")
public record PartitionReassignmentResponse(
        @Schema(description = "Topic name", example = "my-topic")
        String topic,

        @Schema(description = "Partition number", example = "0")
        int partition,

        @Schema(description = "Current replica broker IDs", example = "[1, 2, 3]")
        List<Integer> replicas,

        @Schema(description = "Broker IDs being added", example = "[4]")
        List<Integer> addingReplicas,

        @Schema(description = "Broker IDs being removed", example = "[1]")
        List<Integer> removingReplicas
) {
    public static PartitionReassignmentResponse from(PartitionReassignment reassignment) {
        return new PartitionReassignmentResponse(
                reassignment.topic(),
                reassignment.partition(),
                reassignment.replicas(),
                reassignment.addingReplicas(),
                reassignment.removingReplicas()
        );
    }
}
