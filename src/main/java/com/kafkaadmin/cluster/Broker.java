package com.kafkaadmin.cluster;

/**
 * Domain model representing a Kafka broker.
 *
 * @param id unique broker identifier
 * @param host broker hostname or IP address
 * @param port broker port number
 * @param rack rack identifier for rack-aware replication
 * @param isController whether this broker is the current controller
 */
public record Broker(
        int id,
        String host,
        int port,
        String rack,
        boolean isController
) {
}
