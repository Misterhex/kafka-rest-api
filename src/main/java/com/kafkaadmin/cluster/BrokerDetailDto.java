package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Broker details with configuration")
public record BrokerDetailDto(
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
    public static BrokerDetailDto from(Broker broker, Map<String, String> configs) {
        return new BrokerDetailDto(
                broker.id(),
                broker.host(),
                broker.port(),
                broker.rack(),
                broker.isController(),
                configs);
    }
}
