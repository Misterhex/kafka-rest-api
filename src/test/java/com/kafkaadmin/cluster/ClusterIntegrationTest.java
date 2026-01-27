package com.kafkaadmin.cluster;

import com.kafkaadmin.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class ClusterIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getClusterInfo_shouldReturnClusterDetails() {
        ClusterInfoDto clusterInfo = restTemplate.getForObject(
                "/api/v1/cluster", ClusterInfoDto.class);

        assertThat(clusterInfo).isNotNull();
        assertThat(clusterInfo.clusterId()).isNotEmpty();
        assertThat(clusterInfo.brokerCount()).isGreaterThan(0);
        assertThat(clusterInfo.brokers()).isNotEmpty();
    }

    @Test
    void listBrokers_shouldReturnBrokers() {
        BrokerDto[] brokers = restTemplate.getForObject(
                "/api/v1/cluster/brokers", BrokerDto[].class);

        assertThat(brokers).isNotNull();
        assertThat(brokers).hasSizeGreaterThan(0);
        assertThat(brokers[0].host()).isNotEmpty();
        assertThat(brokers[0].port()).isGreaterThan(0);
    }

    @Test
    void getBroker_shouldReturnBrokerDetails() {
        // First get the list of brokers to find a valid ID
        BrokerDto[] brokers = restTemplate.getForObject(
                "/api/v1/cluster/brokers", BrokerDto[].class);
        assertThat(brokers).isNotEmpty();

        int brokerId = brokers[0].id();

        BrokerDetailDto broker = restTemplate.getForObject(
                "/api/v1/cluster/brokers/" + brokerId, BrokerDetailDto.class);

        assertThat(broker).isNotNull();
        assertThat(broker.id()).isEqualTo(brokerId);
        assertThat(broker.host()).isNotEmpty();
    }

    @Test
    void getBroker_whenNotFound_shouldReturn404() {
        var response = restTemplate.getForEntity(
                "/api/v1/cluster/brokers/99999", String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
