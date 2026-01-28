package com.kafkaadmin.transaction;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Service providing transaction operations.
 */
@Service
public class TransactionService {

    private final KafkaAdminPort kafkaAdminPort;

    public TransactionService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    /**
     * Lists all transactions in the cluster.
     *
     * @return list of transaction listing responses sorted by transactional ID
     */
    public List<TransactionListingResponse> listTransactions() {
        return kafkaAdminPort.listTransactions().stream()
                .map(TransactionListingResponse::from)
                .sorted(Comparator.comparing(TransactionListingResponse::transactionalId))
                .toList();
    }

    /**
     * Gets details for a specific transaction.
     *
     * @param transactionalId the transactional ID
     * @return transaction details
     * @throws TransactionNotFoundException if the transaction does not exist
     */
    public TransactionDetailResponse getTransaction(String transactionalId) {
        TransactionDetail detail = kafkaAdminPort.describeTransaction(transactionalId);
        return TransactionDetailResponse.from(detail);
    }
}
