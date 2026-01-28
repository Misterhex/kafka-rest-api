package com.kafkaadmin.sharegroup;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing share group details for API responses.
 *
 * @param groupId share group identifier
 * @param state current group state
 * @param coordinatorId ID of the coordinator broker
 * @param members list of group members
 */
@Schema(description = "Share group details")
public record ShareGroupDetailResponse(
        @Schema(description = "Share group ID", example = "my-share-group")
        String groupId,

        @Schema(description = "Group state", example = "Stable")
        String state,

        @Schema(description = "Coordinator broker ID", example = "1")
        int coordinatorId,

        @Schema(description = "Group members")
        List<ShareGroupMemberResponse> members
) {
    public static ShareGroupDetailResponse from(ShareGroup group) {
        return new ShareGroupDetailResponse(
                group.groupId(),
                group.state(),
                group.coordinatorId(),
                group.members().stream()
                        .map(ShareGroupMemberResponse::from)
                        .toList());
    }
}
