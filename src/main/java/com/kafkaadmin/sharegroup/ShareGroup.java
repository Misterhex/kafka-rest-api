package com.kafkaadmin.sharegroup;

import java.util.List;

/**
 * Domain model representing a Kafka share group.
 *
 * @param groupId unique identifier for the share group
 * @param state current group state (e.g., Stable, Empty)
 * @param coordinatorId ID of the coordinator broker
 * @param members list of group members
 */
public record ShareGroup(
        String groupId,
        String state,
        int coordinatorId,
        List<ShareGroupMember> members
) {
}
