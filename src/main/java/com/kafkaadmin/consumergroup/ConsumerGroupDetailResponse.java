package com.kafkaadmin.consumergroup;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing consumer group details for API responses.
 *
 * @param groupId consumer group identifier
 * @param state current group state
 * @param partitionAssignor partition assignment strategy
 * @param coordinatorId ID of the coordinator broker
 * @param members list of group members
 */
@Schema(description = "Consumer group details")
public record ConsumerGroupDetailResponse(
        @Schema(description = "Consumer group ID", example = "my-consumer-group")
        String groupId,

        @Schema(description = "Group state", example = "Stable")
        String state,

        @Schema(description = "Partition assignor", example = "range")
        String partitionAssignor,

        @Schema(description = "Coordinator broker ID", example = "1")
        int coordinatorId,

        @Schema(description = "Group members")
        List<ConsumerGroupMemberResponse> members
) {
    public static ConsumerGroupDetailResponse from(ConsumerGroup group) {
        return new ConsumerGroupDetailResponse(
                group.groupId(),
                group.state(),
                group.partitionAssignor(),
                group.coordinatorId(),
                group.members().stream()
                        .map(ConsumerGroupMemberResponse::from)
                        .toList());
    }
}
