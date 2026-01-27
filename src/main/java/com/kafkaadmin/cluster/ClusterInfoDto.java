package com.kafkaadmin.cluster;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Cluster information")
public record ClusterInfoDto(
        @Schema(description = "Cluster ID")
        String clusterId,

        @Schema(description = "Controller broker ID", example = "1")
        int controllerId,

        @Schema(description = "Number of brokers", example = "3")
        int brokerCount,

        @Schema(description = "List of brokers")
        List<BrokerDto> brokers
) {
    public static ClusterInfoDto from(ClusterInfo clusterInfo) {
        return new ClusterInfoDto(
                clusterInfo.clusterId(),
                clusterInfo.controllerId(),
                clusterInfo.brokers().size(),
                clusterInfo.brokers().stream()
                        .map(BrokerDto::from)
                        .toList());
    }
}
