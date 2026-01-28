package com.kafkaadmin.cluster;

import com.kafkaadmin.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class ClusterIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void getClusterInfo_shouldReturnClusterDetails() {
        ClusterInfoResponse clusterInfo = restClient.get()
                .uri("/api/v1/cluster")
                .retrieve()
                .body(ClusterInfoResponse.class);

        assertThat(clusterInfo).isNotNull();
        assertThat(clusterInfo.clusterId()).isNotEmpty();
        assertThat(clusterInfo.brokerCount()).isGreaterThan(0);
        assertThat(clusterInfo.brokers()).isNotEmpty();
    }

    @Test
    void listBrokers_shouldReturnBrokers() {
        BrokerResponse[] brokers = restClient.get()
                .uri("/api/v1/cluster/brokers")
                .retrieve()
                .body(BrokerResponse[].class);

        assertThat(brokers).isNotNull();
        assertThat(brokers).hasSizeGreaterThan(0);
        assertThat(brokers[0].host()).isNotEmpty();
        assertThat(brokers[0].port()).isGreaterThan(0);
    }

    @Test
    void getBroker_shouldReturnBrokerDetails() {
        // First get the list of brokers to find a valid ID
        BrokerResponse[] brokers = restClient.get()
                .uri("/api/v1/cluster/brokers")
                .retrieve()
                .body(BrokerResponse[].class);
        assertThat(brokers).isNotEmpty();

        int brokerId = brokers[0].id();

        BrokerDetailResponse broker = restClient.get()
                .uri("/api/v1/cluster/brokers/" + brokerId)
                .retrieve()
                .body(BrokerDetailResponse.class);

        assertThat(broker).isNotNull();
        assertThat(broker.id()).isEqualTo(brokerId);
        assertThat(broker.host()).isNotEmpty();
    }

    @Test
    void getBroker_whenNotFound_shouldReturn404() {
        var response = restClient.get()
                .uri("/api/v1/cluster/brokers/99999")
                .exchange((request, resp) -> {
                    return resp.getStatusCode();
                });

        assertThat(response.value()).isEqualTo(404);
    }
}
