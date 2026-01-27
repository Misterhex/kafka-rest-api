package com.kafkaadmin.topic;

import java.util.List;

/**
 * Domain model representing partition information for a topic.
 *
 * @param partition partition number
 * @param leader ID of the leader broker
 * @param replicas IDs of all replica brokers
 * @param isr IDs of in-sync replica brokers
 */
public record TopicPartitionInfo(
        int partition,
        int leader,
        List<Integer> replicas,
        List<Integer> isr
) {
}
