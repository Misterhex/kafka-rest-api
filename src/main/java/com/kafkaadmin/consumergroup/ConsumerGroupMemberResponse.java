package com.kafkaadmin.consumergroup;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing a consumer group member for API responses.
 *
 * @param consumerId unique consumer identifier
 * @param clientId client identifier
 * @param host consumer host address
 * @param assignments list of assigned topic-partitions
 */
@Schema(description = "Consumer group member")
public record ConsumerGroupMemberResponse(
        @Schema(description = "Consumer ID")
        String consumerId,

        @Schema(description = "Client ID", example = "consumer-1")
        String clientId,

        @Schema(description = "Host", example = "/192.168.1.100")
        String host,

        @Schema(description = "Assigned partitions")
        List<String> assignments
) {
    public static ConsumerGroupMemberResponse from(ConsumerGroupMember member) {
        return new ConsumerGroupMemberResponse(
                member.consumerId(),
                member.clientId(),
                member.host(),
                member.assignments());
    }
}
