package com.kafkaadmin.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing a transaction listing.
 *
 * @param transactionalId transactional ID
 * @param producerId producer ID
 * @param state transaction state
 */
@Schema(description = "Transaction listing")
public record TransactionListingResponse(
        @Schema(description = "Transactional ID", example = "my-transaction")
        String transactionalId,

        @Schema(description = "Producer ID", example = "12345")
        long producerId,

        @Schema(description = "Transaction state", example = "Ongoing")
        String state
) {
    public static TransactionListingResponse from(TransactionListing listing) {
        return new TransactionListingResponse(
                listing.transactionalId(),
                listing.producerId(),
                listing.state()
        );
    }
}
