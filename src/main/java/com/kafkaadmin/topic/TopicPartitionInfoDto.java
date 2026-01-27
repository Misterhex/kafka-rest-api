package com.kafkaadmin.topic;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Partition information")
public record TopicPartitionInfoDto(
        @Schema(description = "Partition number", example = "0")
        int partition,

        @Schema(description = "Leader broker ID", example = "1")
        int leader,

        @Schema(description = "Replica broker IDs")
        List<Integer> replicas,

        @Schema(description = "In-sync replica broker IDs")
        List<Integer> isr
) {
    public static TopicPartitionInfoDto from(TopicPartitionInfo info) {
        return new TopicPartitionInfoDto(
                info.partition(),
                info.leader(),
                info.replicas(),
                info.isr());
    }
}
