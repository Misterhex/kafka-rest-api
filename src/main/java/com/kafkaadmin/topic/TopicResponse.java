package com.kafkaadmin.topic;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing topic summary for API responses.
 *
 * @param name topic name
 * @param partitionCount number of partitions
 * @param replicationFactor number of replicas per partition
 * @param internal whether this is an internal Kafka topic
 */
@Schema(description = "Topic summary")
public record TopicResponse(
        @Schema(description = "Topic name", example = "my-topic")
        String name,

        @Schema(description = "Number of partitions", example = "3")
        int partitionCount,

        @Schema(description = "Replication factor", example = "2")
        int replicationFactor,

        @Schema(description = "Whether this is an internal topic", example = "false")
        boolean internal
) {
    public static TopicResponse from(Topic topic) {
        return new TopicResponse(
                topic.name(),
                topic.partitionCount(),
                topic.replicationFactor(),
                topic.internal());
    }
}
