package com.kafkaadmin.transaction;

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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TransactionController controller = new TransactionController(transactionService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listTransactions_shouldReturnTransactions() throws Exception {
        // Given
        List<TransactionListingResponse> transactions = List.of(
                new TransactionListingResponse("txn-1", 12345L, "Ongoing"),
                new TransactionListingResponse("txn-2", 12346L, "PrepareCommit")
        );
        when(transactionService.listTransactions()).thenReturn(transactions);

        // When/Then
        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].transactionalId").value("txn-1"))
                .andExpect(jsonPath("$[0].state").value("Ongoing"));
    }

    @Test
    void getTransaction_shouldReturnTransactionDetails() throws Exception {
        // Given
        TransactionDetailResponse detail = new TransactionDetailResponse(
                "my-txn",
                1,
                "Ongoing",
                12345L,
                1,
                60000L,
                1704067200000L,
                List.of(new TransactionTopicPartitionResponse("topic-a", 0))
        );
        when(transactionService.getTransaction("my-txn")).thenReturn(detail);

        // When/Then
        mockMvc.perform(get("/api/v1/transactions/my-txn")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionalId").value("my-txn"))
                .andExpect(jsonPath("$.state").value("Ongoing"))
                .andExpect(jsonPath("$.topicPartitions.length()").value(1));
    }

    @Test
    void getTransaction_whenNotFound_shouldReturn404() throws Exception {
        // Given
        when(transactionService.getTransaction("non-existent"))
                .thenThrow(new TransactionNotFoundException("non-existent"));

        // When/Then
        mockMvc.perform(get("/api/v1/transactions/non-existent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
