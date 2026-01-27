package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing broker summary for API responses.
 *
 * @param id broker identifier
 * @param host broker hostname
 * @param port broker port
 * @param rack rack identifier
 * @param isController whether this broker is the controller
 */
@Schema(description = "Broker information")
public record BrokerResponse(
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
    public static BrokerResponse from(Broker broker) {
        return new BrokerResponse(
                broker.id(),
                broker.host(),
                broker.port(),
                broker.rack(),
                broker.isController());
    }
}
