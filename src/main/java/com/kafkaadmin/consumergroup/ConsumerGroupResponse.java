package com.kafkaadmin.consumergroup;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing consumer group summary for API responses.
 *
 * @param groupId consumer group identifier
 * @param state current group state
 * @param memberCount number of members in the group
 */
@Schema(description = "Consumer group summary")
public record ConsumerGroupResponse(
        @Schema(description = "Consumer group ID", example = "my-consumer-group")
        String groupId,

        @Schema(description = "Group state", example = "Stable")
        String state,

        @Schema(description = "Number of members", example = "3")
        int memberCount
) {
    public static ConsumerGroupResponse from(ConsumerGroup group) {
        return new ConsumerGroupResponse(
                group.groupId(),
                group.state(),
                group.members().size());
    }
}
