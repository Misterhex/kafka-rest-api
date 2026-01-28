package com.kafkaadmin.transaction;

import com.kafkaadmin.common.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for transaction operations.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions", description = "Kafka transaction operations")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Lists all transactions in the cluster.
     *
     * @return list of transactions sorted by transactional ID
     */
    @GetMapping
    @Operation(summary = "List all transactions",
            description = "Returns a list of all transactions in the Kafka cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TransactionListingResponse>> listTransactions() {
        return ResponseEntity.ok(transactionService.listTransactions());
    }

    /**
     * Gets details for a specific transaction.
     *
     * @param transactionalId the transactional ID
     * @return transaction details
     */
    @GetMapping("/{transactionalId}")
    @Operation(summary = "Get transaction details",
            description = "Returns detailed information about a specific transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction"),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TransactionDetailResponse> getTransaction(
            @Parameter(description = "Transactional ID", required = true)
            @PathVariable String transactionalId) {
        return ResponseEntity.ok(transactionService.getTransaction(transactionalId));
    }
}
