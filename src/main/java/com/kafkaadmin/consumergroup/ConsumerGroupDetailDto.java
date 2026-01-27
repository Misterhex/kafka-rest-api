package com.kafkaadmin.consumergroup;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Consumer group details")
public record ConsumerGroupDetailDto(
        @Schema(description = "Consumer group ID", example = "my-consumer-group")
        String groupId,

        @Schema(description = "Group state", example = "Stable")
        String state,

        @Schema(description = "Partition assignor", example = "range")
        String partitionAssignor,

        @Schema(description = "Coordinator broker ID", example = "1")
        int coordinatorId,

        @Schema(description = "Group members")
        List<ConsumerGroupMemberDto> members
) {
    public static ConsumerGroupDetailDto from(ConsumerGroup group) {
        return new ConsumerGroupDetailDto(
                group.groupId(),
                group.state(),
                group.partitionAssignor(),
                group.coordinatorId(),
                group.members().stream()
                        .map(ConsumerGroupMemberDto::from)
                        .toList());
    }
}
