package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Broker information")
public record BrokerDto(
        @Schema(description = "Broker ID", example = "1")
        int id,

        @Schema(description = "Broker host", example = "kafka-broker-1")
        String host,

        @Schema(description = "Broker port", example = "9092")
        int port,

        @Schema(description = "Rack ID", example = "rack-1")
        String rack,

        @Schema(description = "Whether this broker is the controller", example = "true")
        boolean isController
) {
    public static BrokerDto from(Broker broker) {
        return new BrokerDto(
                broker.id(),
                broker.host(),
                broker.port(),
                broker.rack(),
                broker.isController());
    }
}
