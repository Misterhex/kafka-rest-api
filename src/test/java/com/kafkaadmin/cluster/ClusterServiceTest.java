package com.kafkaadmin.cluster;

import com.kafkaadmin.common.KafkaAdminPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClusterServiceTest {

    @Mock
    private KafkaAdminPort kafkaAdminPort;

    private ClusterService clusterService;

    @BeforeEach
    void setUp() {
        clusterService = new ClusterService(kafkaAdminPort);
    }

    @Test
    void getClusterInfo_shouldReturnClusterDetails() {
        // Given
        ClusterInfo clusterInfo = new ClusterInfo(
                "test-cluster-id",
                1,
                List.of(
                        new Broker(1, "broker-1", 9092, "rack-1", true),
                        new Broker(2, "broker-2", 9092, "rack-2", false)
                ));

        when(kafkaAdminPort.getClusterInfo()).thenReturn(clusterInfo);

        // When
        ClusterInfoDto result = clusterService.getClusterInfo();

        // Then
        assertThat(result.clusterId()).isEqualTo("test-cluster-id");
        assertThat(result.controllerId()).isEqualTo(1);
        assertThat(result.brokerCount()).isEqualTo(2);
        assertThat(result.brokers()).hasSize(2);
    }

    @Test
    void listBrokers_shouldReturnSortedBrokers() {
        // Given
        List<Broker> brokers = List.of(
                new Broker(3, "broker-3", 9092, null, false),
                new Broker(1, "broker-1", 9092, "rack-1", true),
                new Broker(2, "broker-2", 9092, "rack-2", false)
        );

        when(kafkaAdminPort.listBrokers()).thenReturn(brokers);

        // When
        List<BrokerDto> result = clusterService.listBrokers();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).id()).isEqualTo(1);
        assertThat(result.get(1).id()).isEqualTo(2);
        assertThat(result.get(2).id()).isEqualTo(3);
    }

    @Test
    void getBroker_shouldReturnBrokerWithConfigs() {
        // Given
        int brokerId = 1;
        Broker broker = new Broker(brokerId, "broker-1", 9092, "rack-1", true);
        Map<String, String> configs = Map.of(
                "log.retention.hours", "168",
                "num.partitions", "1"
        );

        when(kafkaAdminPort.getBroker(brokerId)).thenReturn(broker);
        when(kafkaAdminPort.getBrokerConfigs(brokerId)).thenReturn(configs);

        // When
        BrokerDetailDto result = clusterService.getBroker(brokerId);

        // Then
        assertThat(result.id()).isEqualTo(brokerId);
        assertThat(result.host()).isEqualTo("broker-1");
        assertThat(result.port()).isEqualTo(9092);
        assertThat(result.rack()).isEqualTo("rack-1");
        assertThat(result.isController()).isTrue();
        assertThat(result.configs()).containsEntry("log.retention.hours", "168");
    }

    @Test
    void getBroker_whenNotFound_shouldThrowException() {
        // Given
        int brokerId = 999;
        when(kafkaAdminPort.getBroker(brokerId))
                .thenThrow(new BrokerNotFoundException(brokerId));

        // When/Then
        assertThatThrownBy(() -> clusterService.getBroker(brokerId))
                .isInstanceOf(BrokerNotFoundException.class)
                .hasMessageContaining("999");
    }
}
