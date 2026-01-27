package com.kafkaadmin.consumergroup;

import java.util.List;

public record ConsumerGroup(
        String groupId,
        String state,
        String partitionAssignor,
        int coordinatorId,
        List<ConsumerGroupMember> members
) {
}
