package com.kafkaadmin.cluster;

public record Broker(
        int id,
        String host,
        int port,
        String rack,
        boolean isController
) {
}
