package com.kafkaadmin.consumergroup;

import java.util.List;

/**
 * Domain model representing a Kafka consumer group.
 *
 * @param groupId unique identifier for the consumer group
 * @param state current group state (e.g., Stable, Rebalancing)
 * @param partitionAssignor partition assignment strategy
 * @param coordinatorId ID of the coordinator broker
 * @param members list of group members
 */
public record ConsumerGroup(
        String groupId,
        String state,
        String partitionAssignor,
        int coordinatorId,
        List<ConsumerGroupMember> members
) {
}
