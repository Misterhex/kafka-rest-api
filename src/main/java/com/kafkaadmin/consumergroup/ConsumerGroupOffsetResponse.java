package com.kafkaadmin.consumergroup;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing consumer group offset information for API responses.
 *
 * @param topic topic name
 * @param partition partition number
 * @param currentOffset last committed offset
 * @param endOffset latest offset in the partition
 * @param lag difference between end offset and current offset
 */
@Schema(description = "Consumer group offset for a partition")
public record ConsumerGroupOffsetResponse(
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
    public static ConsumerGroupOffsetResponse from(ConsumerGroupOffset offset) {
        return new ConsumerGroupOffsetResponse(
                offset.topic(),
                offset.partition(),
                offset.currentOffset(),
                offset.endOffset(),
                offset.lag());
    }
}
