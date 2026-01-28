package com.kafkaadmin.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing a topic-partition in a transaction.
 *
 * @param topic topic name
 * @param partition partition number
 */
@Schema(description = "Topic-partition in a transaction")
public record TransactionTopicPartitionResponse(
        @Schema(description = "Topic name", example = "my-topic")
        String topic,

        @Schema(description = "Partition number", example = "0")
        int partition
) {
    public static TransactionTopicPartitionResponse from(TransactionTopicPartition tp) {
        return new TransactionTopicPartitionResponse(tp.topic(), tp.partition());
    }
}
