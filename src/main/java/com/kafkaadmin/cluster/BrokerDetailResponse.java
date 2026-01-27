package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * Response representing broker details with configuration for API responses.
 *
 * @param id broker identifier
 * @param host broker hostname
 * @param port broker port
 * @param rack rack identifier
 * @param isController whether this broker is the controller
 * @param configs non-default broker configuration key-value pairs
 */
@Schema(description = "Broker details with configuration")
public record BrokerDetailResponse(
        @Schema(description = "Broker ID", example = "1")
        int id,

        @Schema(description = "Broker host", example = "kafka-broker-1")
        String host,

        @Schema(description = "Broker port", example = "9092")
        int port,

        @Schema(description = "Rack ID", example = "rack-1")
        String rack,

        @Schema(description = "Whether this broker is the controller", example = "true")
        boolean isController,

        @Schema(description = "Non-default broker configuration")
        Map<String, String> configs
) {
    public static BrokerDetailResponse from(Broker broker, Map<String, String> configs) {
        return new BrokerDetailResponse(
                broker.id(),
                broker.host(),
                broker.port(),
                broker.rack(),
                broker.isController(),
                configs);
    }
}
