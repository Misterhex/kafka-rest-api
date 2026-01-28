package com.kafkaadmin.quota;

import com.kafkaadmin.common.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for client quota operations.
 */
@RestController
@RequestMapping("/api/v1/quotas")
@Tag(name = "Quotas", description = "Kafka client quota operations")
public class QuotaController {

    private final QuotaService quotaService;

    public QuotaController(QuotaService quotaService) {
        this.quotaService = quotaService;
    }

    /**
     * Lists all client quotas in the cluster.
     *
     * @return list of client quotas sorted by entity type and name
     */
    @GetMapping
    @Operation(summary = "List all client quotas",
            description = "Returns a list of all client quota configurations in the Kafka cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved quotas"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ClientQuotaResponse>> listQuotas() {
        return ResponseEntity.ok(quotaService.listQuotas());
    }
}
