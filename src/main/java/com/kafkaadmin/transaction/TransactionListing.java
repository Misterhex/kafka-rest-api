package com.kafkaadmin.transaction;

/**
 * Domain model representing a transaction listing.
 *
 * @param transactionalId transactional ID
 * @param producerId producer ID
 * @param state transaction state
 */
public record TransactionListing(
        String transactionalId,
        long producerId,
        String state
) {
}
