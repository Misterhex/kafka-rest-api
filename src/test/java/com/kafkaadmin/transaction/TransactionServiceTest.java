package com.kafkaadmin.transaction;

import com.kafkaadmin.common.KafkaAdminPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private KafkaAdminPort kafkaAdminPort;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(kafkaAdminPort);
    }

    @Test
    void listTransactions_shouldReturnSortedTransactions() {
        // Given
        List<TransactionListing> listings = List.of(
                new TransactionListing("txn-b", 2L, "Ongoing"),
                new TransactionListing("txn-a", 1L, "PrepareCommit")
        );

        when(kafkaAdminPort.listTransactions()).thenReturn(listings);

        // When
        List<TransactionListingResponse> result = transactionService.listTransactions();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).transactionalId()).isEqualTo("txn-a");
        assertThat(result.get(1).transactionalId()).isEqualTo("txn-b");
    }

    @Test
    void getTransaction_shouldReturnTransactionDetails() {
        // Given
        String transactionalId = "my-txn";
        TransactionDetail detail = new TransactionDetail(
                transactionalId,
                1,
                "Ongoing",
                12345L,
                1,
                60000L,
                1704067200000L,
                List.of(new TransactionTopicPartition("topic-a", 0))
        );

        when(kafkaAdminPort.describeTransaction(transactionalId)).thenReturn(detail);

        // When
        TransactionDetailResponse result = transactionService.getTransaction(transactionalId);

        // Then
        assertThat(result.transactionalId()).isEqualTo(transactionalId);
        assertThat(result.state()).isEqualTo("Ongoing");
        assertThat(result.topicPartitions()).hasSize(1);
    }

    @Test
    void getTransaction_whenNotFound_shouldThrowException() {
        // Given
        String transactionalId = "non-existent";
        when(kafkaAdminPort.describeTransaction(transactionalId))
                .thenThrow(new TransactionNotFoundException(transactionalId));

        // When/Then
        assertThatThrownBy(() -> transactionService.getTransaction(transactionalId))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessageContaining(transactionalId);
    }
}
