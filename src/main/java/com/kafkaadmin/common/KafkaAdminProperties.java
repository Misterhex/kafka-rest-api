package com.kafkaadmin.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
