package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing a Kafka feature.
 *
 * @param name feature name
 * @param minVersion minimum supported version
 * @param maxVersion maximum supported version
 * @param finalizedVersion currently finalized version
 */
@Schema(description = "Kafka feature information")
public record KafkaFeatureResponse(
        @Schema(description = "Feature name", example = "metadata.version")
        String name,

        @Schema(description = "Minimum supported version", example = "1")
        short minVersion,

        @Schema(description = "Maximum supported version", example = "16")
        short maxVersion,

        @Schema(description = "Currently finalized version", example = "16")
        short finalizedVersion
) {
    public static KafkaFeatureResponse from(KafkaFeature feature) {
        return new KafkaFeatureResponse(
                feature.name(),
                feature.minVersion(),
                feature.maxVersion(),
                feature.finalizedVersion()
        );
    }
}
