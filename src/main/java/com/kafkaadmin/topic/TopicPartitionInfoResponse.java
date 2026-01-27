package com.kafkaadmin.topic;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing partition information for API responses.
 *
 * @param partition partition number
 * @param leader ID of the leader broker
 * @param replicas IDs of all replica brokers
 * @param isr IDs of in-sync replica brokers
 */
@Schema(description = "Partition information")
public record TopicPartitionInfoResponse(
        @Schema(description = "Partition number", example = "0")
        int partition,

        @Schema(description = "Leader broker ID", example = "1")
        int leader,

        @Schema(description = "Replica broker IDs")
        List<Integer> replicas,

        @Schema(description = "In-sync replica broker IDs")
        List<Integer> isr
) {
    public static TopicPartitionInfoResponse from(TopicPartitionInfo info) {
        return new TopicPartitionInfoResponse(
                info.partition(),
                info.leader(),
                info.replicas(),
                info.isr());
    }
}
