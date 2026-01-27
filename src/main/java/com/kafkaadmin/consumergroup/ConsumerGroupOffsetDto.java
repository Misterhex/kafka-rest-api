package com.kafkaadmin.consumergroup;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Consumer group offset for a partition")
public record ConsumerGroupOffsetDto(
        @Schema(description = "Topic name", example = "my-topic")
        String topic,

        @Schema(description = "Partition number", example = "0")
        int partition,

        @Schema(description = "Current committed offset", example = "1000")
        long currentOffset,

        @Schema(description = "End offset (latest)", example = "1050")
        long endOffset,

        @Schema(description = "Consumer lag", example = "50")
        long lag
) {
    public static ConsumerGroupOffsetDto from(ConsumerGroupOffset offset) {
        return new ConsumerGroupOffsetDto(
                offset.topic(),
                offset.partition(),
                offset.currentOffset(),
                offset.endOffset(),
                offset.lag());
    }
}
