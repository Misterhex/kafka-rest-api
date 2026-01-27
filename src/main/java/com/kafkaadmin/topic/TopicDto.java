package com.kafkaadmin.topic;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Topic summary")
public record TopicDto(
        @Schema(description = "Topic name", example = "my-topic")
        String name,

        @Schema(description = "Number of partitions", example = "3")
        int partitionCount,

        @Schema(description = "Replication factor", example = "2")
        int replicationFactor,

        @Schema(description = "Whether this is an internal topic", example = "false")
        boolean internal
) {
    public static TopicDto from(Topic topic) {
        return new TopicDto(
                topic.name(),
                topic.partitionCount(),
                topic.replicationFactor(),
                topic.internal());
    }
}
