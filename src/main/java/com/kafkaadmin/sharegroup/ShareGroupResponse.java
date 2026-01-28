package com.kafkaadmin.sharegroup;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing share group summary for API responses.
 *
 * @param groupId share group identifier
 * @param state current group state
 * @param memberCount number of members in the group
 */
@Schema(description = "Share group summary")
public record ShareGroupResponse(
        @Schema(description = "Share group ID", example = "my-share-group")
        String groupId,

        @Schema(description = "Group state", example = "Stable")
        String state,

        @Schema(description = "Number of members", example = "3")
        int memberCount
) {
    public static ShareGroupResponse from(ShareGroup group) {
        return new ShareGroupResponse(
                group.groupId(),
                group.state(),
                group.members().size());
    }
}
