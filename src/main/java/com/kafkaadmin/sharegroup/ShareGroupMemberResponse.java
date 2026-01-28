package com.kafkaadmin.sharegroup;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing a share group member for API responses.
 *
 * @param consumerId unique consumer identifier
 * @param clientId client identifier
 * @param host consumer host address
 * @param assignments list of assigned topic-partitions
 */
@Schema(description = "Share group member")
public record ShareGroupMemberResponse(
        @Schema(description = "Consumer ID")
        String consumerId,

        @Schema(description = "Client ID", example = "consumer-1")
        String clientId,

        @Schema(description = "Host", example = "/192.168.1.100")
        String host,

        @Schema(description = "Assigned partitions")
        List<String> assignments
) {
    public static ShareGroupMemberResponse from(ShareGroupMember member) {
        return new ShareGroupMemberResponse(
                member.consumerId(),
                member.clientId(),
                member.host(),
                member.assignments());
    }
}
