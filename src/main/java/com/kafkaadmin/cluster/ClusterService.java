package com.kafkaadmin.cluster;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class ClusterService {

    private final KafkaAdminPort kafkaAdminPort;

    public ClusterService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    public ClusterInfoDto getClusterInfo() {
        ClusterInfo clusterInfo = kafkaAdminPort.getClusterInfo();
        return ClusterInfoDto.from(clusterInfo);
    }

    public List<BrokerDto> listBrokers() {
        return kafkaAdminPort.listBrokers().stream()
                .map(BrokerDto::from)
                .sorted(Comparator.comparingInt(BrokerDto::id))
                .toList();
    }

    public BrokerDetailDto getBroker(int brokerId) {
        Broker broker = kafkaAdminPort.getBroker(brokerId);
        Map<String, String> configs = kafkaAdminPort.getBrokerConfigs(brokerId);
        return BrokerDetailDto.from(broker, configs);
    }
}
