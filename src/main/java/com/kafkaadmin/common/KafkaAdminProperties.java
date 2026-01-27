package com.kafkaadmin.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for connecting to Kafka.
 *
 * <p>Properties are bound from the {@code kafka.admin} prefix in application configuration.
 *
 * @param bootstrapServers comma-separated list of Kafka broker addresses
 * @param requestTimeoutMs timeout in milliseconds for individual requests
 * @param defaultApiTimeoutMs default timeout in milliseconds for API calls
 * @param clientId client identifier sent with requests
 */
@ConfigurationProperties(prefix = "kafka.admin")
public record KafkaAdminProperties(
        String bootstrapServers,
        int requestTimeoutMs,
        int defaultApiTimeoutMs,
        String clientId
) {
    public KafkaAdminProperties {
        if (bootstrapServers == null || bootstrapServers.isBlank()) {
            bootstrapServers = "localhost:9092";
        }
        if (requestTimeoutMs <= 0) {
            requestTimeoutMs = 30000;
        }
        if (defaultApiTimeoutMs <= 0) {
            defaultApiTimeoutMs = 60000;
        }
        if (clientId == null || clientId.isBlank()) {
            clientId = "kafka-admin-api";
        }
    }
}
