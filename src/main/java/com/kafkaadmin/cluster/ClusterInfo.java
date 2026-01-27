package com.kafkaadmin.cluster;

import java.util.List;

public record ClusterInfo(
        String clusterId,
        int controllerId,
        List<Broker> brokers
) {
}
