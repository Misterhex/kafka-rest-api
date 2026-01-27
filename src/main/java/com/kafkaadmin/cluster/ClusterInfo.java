package com.kafkaadmin.cluster;

import java.util.List;

/**
 * Domain model representing Kafka cluster information.
 *
 * @param clusterId unique identifier for the cluster
 * @param controllerId ID of the controller broker
 * @param brokers list of brokers in the cluster
 */
public record ClusterInfo(
        String clusterId,
        int controllerId,
        List<Broker> brokers
) {
}
