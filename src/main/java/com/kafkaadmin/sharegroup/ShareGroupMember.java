package com.kafkaadmin.sharegroup;

import java.util.List;

/**
 * Domain model representing a member of a share group.
 *
 * @param consumerId unique consumer identifier
 * @param clientId client identifier
 * @param host consumer host address
 * @param assignments list of assigned topic-partitions
 */
public record ShareGroupMember(
        String consumerId,
        String clientId,
        String host,
        List<String> assignments
) {
}
