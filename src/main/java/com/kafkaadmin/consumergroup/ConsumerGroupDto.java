package com.kafkaadmin.consumergroup;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Consumer group summary")
public record ConsumerGroupDto(
        @Schema(description = "Consumer group ID", example = "my-consumer-group")
        String groupId,

        @Schema(description = "Group state", example = "Stable")
        String state,

        @Schema(description = "Number of members", example = "3")
        int memberCount
) {
    public static ConsumerGroupDto from(ConsumerGroup group) {
        return new ConsumerGroupDto(
                group.groupId(),
                group.state(),
                group.members().size());
    }
}
