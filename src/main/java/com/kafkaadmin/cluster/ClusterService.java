package com.kafkaadmin.cluster;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Service providing cluster management operations.
 */
@Service
public class ClusterService {

    private final KafkaAdminPort kafkaAdminPort;

    /**
     * Creates a service with the given Kafka admin port.
     *
     * @param kafkaAdminPort the Kafka admin port
     */
    public ClusterService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    /**
     * Retrieves cluster information.
     *
     * @return cluster information DTO
     */
    public ClusterInfoResponse getClusterInfo() {
        ClusterInfo clusterInfo = kafkaAdminPort.getClusterInfo();
        return ClusterInfoResponse.from(clusterInfo);
    }

    /**
     * Lists all brokers in the cluster.
     *
     * @return list of broker DTOs sorted by ID
     */
    public List<BrokerResponse> listBrokers() {
        return kafkaAdminPort.listBrokers().stream()
                .map(BrokerResponse::from)
                .sorted(Comparator.comparingInt(BrokerResponse::id))
                .toList();
    }

    /**
     * Retrieves details for a specific broker.
     *
     * @param brokerId the broker ID
     * @return broker detail DTO
     * @throws BrokerNotFoundException if the broker does not exist
     */
    public BrokerDetailResponse getBroker(int brokerId) {
        Broker broker = kafkaAdminPort.getBroker(brokerId);
        Map<String, String> configs = kafkaAdminPort.getBrokerConfigs(brokerId);
        return BrokerDetailResponse.from(broker, configs);
    }
}
