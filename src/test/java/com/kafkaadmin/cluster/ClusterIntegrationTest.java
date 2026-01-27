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
        ClusterInfoResponse clusterInfo = restTemplate.getForObject(
                "/api/v1/cluster", ClusterInfoResponse.class);

        assertThat(clusterInfo).isNotNull();
        assertThat(clusterInfo.clusterId()).isNotEmpty();
        assertThat(clusterInfo.brokerCount()).isGreaterThan(0);
        assertThat(clusterInfo.brokers()).isNotEmpty();
    }

    @Test
    void listBrokers_shouldReturnBrokers() {
        BrokerResponse[] brokers = restTemplate.getForObject(
                "/api/v1/cluster/brokers", BrokerResponse[].class);

        assertThat(brokers).isNotNull();
        assertThat(brokers).hasSizeGreaterThan(0);
        assertThat(brokers[0].host()).isNotEmpty();
        assertThat(brokers[0].port()).isGreaterThan(0);
    }

    @Test
    void getBroker_shouldReturnBrokerDetails() {
        // First get the list of brokers to find a valid ID
        BrokerResponse[] brokers = restTemplate.getForObject(
                "/api/v1/cluster/brokers", BrokerResponse[].class);
        assertThat(brokers).isNotEmpty();

        int brokerId = brokers[0].id();

        BrokerDetailResponse broker = restTemplate.getForObject(
                "/api/v1/cluster/brokers/" + brokerId, BrokerDetailResponse.class);

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
