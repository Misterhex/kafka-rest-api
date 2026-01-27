package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing cluster information for API responses.
 *
 * @param clusterId unique identifier for the cluster
 * @param controllerId ID of the controller broker
 * @param brokerCount number of brokers in the cluster
 * @param brokers list of broker details
 */
@Schema(description = "Cluster information")
public record ClusterInfoResponse(
        @Schema(description = "Cluster ID")
        String clusterId,

        @Schema(description = "Controller broker ID", example = "1")
        int controllerId,

        @Schema(description = "Number of brokers", example = "3")
        int brokerCount,

        @Schema(description = "List of brokers")
        List<BrokerResponse> brokers
) {
    public static ClusterInfoResponse from(ClusterInfo clusterInfo) {
        return new ClusterInfoResponse(
                clusterInfo.clusterId(),
                clusterInfo.controllerId(),
                clusterInfo.brokers().size(),
                clusterInfo.brokers().stream()
                        .map(BrokerResponse::from)
                        .toList());
    }
}
