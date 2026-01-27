package com.kafkaadmin.consumergroup;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Consumer group member")
public record ConsumerGroupMemberDto(
        @Schema(description = "Consumer ID")
        String consumerId,

        @Schema(description = "Client ID", example = "consumer-1")
        String clientId,

        @Schema(description = "Host", example = "/192.168.1.100")
        String host,

        @Schema(description = "Assigned partitions")
        List<String> assignments
) {
    public static ConsumerGroupMemberDto from(ConsumerGroupMember member) {
        return new ConsumerGroupMemberDto(
                member.consumerId(),
                member.clientId(),
                member.host(),
                member.assignments());
    }
}
