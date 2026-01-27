package com.kafkaadmin.cluster;

import com.kafkaadmin.common.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClusterControllerTest {

    @Mock
    private ClusterService clusterService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ClusterController controller = new ClusterController(clusterService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getClusterInfo_shouldReturnClusterDetails() throws Exception {
        // Given
        ClusterInfoResponse clusterInfo = new ClusterInfoResponse(
                "test-cluster-id",
                1,
                3,
                List.of(
                        new BrokerResponse(1, "broker-1", 9092, "rack-1", true),
                        new BrokerResponse(2, "broker-2", 9092, "rack-2", false),
                        new BrokerResponse(3, "broker-3", 9092, "rack-3", false)
                )
        );
        when(clusterService.getClusterInfo()).thenReturn(clusterInfo);

        // When/Then
        mockMvc.perform(get("/api/v1/cluster")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clusterId").value("test-cluster-id"))
                .andExpect(jsonPath("$.controllerId").value(1))
                .andExpect(jsonPath("$.brokerCount").value(3))
                .andExpect(jsonPath("$.brokers.length()").value(3));
    }

    @Test
    void listBrokers_shouldReturnBrokers() throws Exception {
        // Given
        List<BrokerResponse> brokers = List.of(
                new BrokerResponse(1, "broker-1", 9092, "rack-1", true),
                new BrokerResponse(2, "broker-2", 9092, "rack-2", false)
        );
        when(clusterService.listBrokers()).thenReturn(brokers);

        // When/Then
        mockMvc.perform(get("/api/v1/cluster/brokers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].host").value("broker-1"))
                .andExpect(jsonPath("$[0].isController").value(true));
    }

    @Test
    void getBroker_shouldReturnBrokerDetails() throws Exception {
        // Given
        BrokerDetailResponse broker = new BrokerDetailResponse(
                1,
                "broker-1",
                9092,
                "rack-1",
                true,
                Map.of("log.retention.hours", "168")
        );
        when(clusterService.getBroker(1)).thenReturn(broker);

        // When/Then
        mockMvc.perform(get("/api/v1/cluster/brokers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.host").value("broker-1"))
                .andExpect(jsonPath("$.isController").value(true))
                .andExpect(jsonPath("$.configs.['log.retention.hours']").value("168"));
    }

    @Test
    void getBroker_whenNotFound_shouldReturn404() throws Exception {
        // Given
        when(clusterService.getBroker(999))
                .thenThrow(new BrokerNotFoundException(999));

        // When/Then
        mockMvc.perform(get("/api/v1/cluster/brokers/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Broker with ID '999' not found"));
    }
}
