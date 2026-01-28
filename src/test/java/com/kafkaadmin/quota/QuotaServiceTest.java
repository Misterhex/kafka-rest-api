package com.kafkaadmin.quota;

import com.kafkaadmin.common.KafkaAdminPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuotaServiceTest {

    @Mock
    private KafkaAdminPort kafkaAdminPort;

    private QuotaService quotaService;

    @BeforeEach
    void setUp() {
        quotaService = new QuotaService(kafkaAdminPort);
    }

    @Test
    void listQuotas_shouldReturnSortedQuotas() {
        // Given
        List<ClientQuota> quotas = List.of(
                new ClientQuota("user", "bob", Map.of("producer_byte_rate", 1048576.0)),
                new ClientQuota("client-id", "client-a", Map.of("consumer_byte_rate", 2097152.0)),
                new ClientQuota("user", "alice", Map.of("producer_byte_rate", 1048576.0))
        );

        when(kafkaAdminPort.describeClientQuotas()).thenReturn(quotas);

        // When
        List<ClientQuotaResponse> result = quotaService.listQuotas();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).entityType()).isEqualTo("client-id");
        assertThat(result.get(1).entityType()).isEqualTo("user");
        assertThat(result.get(1).entityName()).isEqualTo("alice");
        assertThat(result.get(2).entityName()).isEqualTo("bob");
    }

    @Test
    void listQuotas_whenEmpty_shouldReturnEmptyList() {
        // Given
        when(kafkaAdminPort.describeClientQuotas()).thenReturn(List.of());

        // When
        List<ClientQuotaResponse> result = quotaService.listQuotas();

        // Then
        assertThat(result).isEmpty();
    }
}
