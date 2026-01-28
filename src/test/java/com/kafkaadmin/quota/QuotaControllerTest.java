package com.kafkaadmin.quota;

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
class QuotaControllerTest {

    @Mock
    private QuotaService quotaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        QuotaController controller = new QuotaController(quotaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listQuotas_shouldReturnQuotas() throws Exception {
        // Given
        List<ClientQuotaResponse> quotas = List.of(
                new ClientQuotaResponse("user", "alice", Map.of("producer_byte_rate", 1048576.0))
        );
        when(quotaService.listQuotas()).thenReturn(quotas);

        // When/Then
        mockMvc.perform(get("/api/v1/quotas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].entityType").value("user"))
                .andExpect(jsonPath("$[0].entityName").value("alice"));
    }
}
