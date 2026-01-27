package com.kafkaadmin.topic;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

/**
 * Response representing topic details for API responses.
 *
 * @param name topic name
 * @param partitionCount number of partitions
 * @param replicationFactor number of replicas per partition
 * @param internal whether this is an internal Kafka topic
 * @param configs non-default topic configuration
 * @param partitions partition details
 */
@Schema(description = "Topic details")
public record TopicDetailResponse(
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
        List<TopicPartitionInfoResponse> partitions
) {
    public static TopicDetailResponse from(Topic topic) {
        return new TopicDetailResponse(
                topic.name(),
                topic.partitionCount(),
                topic.replicationFactor(),
                topic.internal(),
                topic.configs(),
                topic.partitions().stream()
                        .map(TopicPartitionInfoResponse::from)
                        .toList());
    }
}
