package com.kafkaadmin.transaction;

/**
 * Exception thrown when a transaction is not found.
 */
public class TransactionNotFoundException extends RuntimeException {

    private final String transactionalId;

    public TransactionNotFoundException(String transactionalId) {
        super("Transaction with ID '" + transactionalId + "' not found");
        this.transactionalId = transactionalId;
    }

    public String getTransactionalId() {
        return transactionalId;
    }
}
