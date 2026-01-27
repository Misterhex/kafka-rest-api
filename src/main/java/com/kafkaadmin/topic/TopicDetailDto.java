package com.kafkaadmin.topic;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "Topic details")
public record TopicDetailDto(
        @Schema(description = "Topic name", example = "my-topic")
        String name,

        @Schema(description = "Number of partitions", example = "3")
        int partitionCount,

        @Schema(description = "Replication factor", example = "2")
        int replicationFactor,

        @Schema(description = "Whether this is an internal topic", example = "false")
        boolean internal,

        @Schema(description = "Topic configuration overrides")
        Map<String, String> configs,

        @Schema(description = "Partition details")
        List<TopicPartitionInfoDto> partitions
) {
    public static TopicDetailDto from(Topic topic) {
        return new TopicDetailDto(
                topic.name(),
                topic.partitionCount(),
                topic.replicationFactor(),
                topic.internal(),
                topic.configs(),
                topic.partitions().stream()
                        .map(TopicPartitionInfoDto::from)
                        .toList());
    }
}
