package com.kafkaadmin.cluster;

/**
 * Domain model representing a Kafka feature.
 *
 * @param name feature name
 * @param minVersion minimum supported version
 * @param maxVersion maximum supported version
 * @param finalizedVersion currently finalized version (0 if not finalized)
 */
public record KafkaFeature(
        String name,
        short minVersion,
        short maxVersion,
        short finalizedVersion
) {
}
