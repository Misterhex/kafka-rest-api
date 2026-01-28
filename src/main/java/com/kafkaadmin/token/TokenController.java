package com.kafkaadmin.token;

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
 * REST controller for delegation token operations.
 */
@RestController
@RequestMapping("/api/v1/tokens")
@Tag(name = "Tokens", description = "Kafka delegation token operations")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Lists all delegation tokens in the cluster.
     *
     * @return list of delegation tokens sorted by token ID
     */
    @GetMapping
    @Operation(summary = "List all delegation tokens",
            description = "Returns a list of all delegation tokens in the Kafka cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tokens"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<DelegationTokenResponse>> listTokens() {
        return ResponseEntity.ok(tokenService.listTokens());
    }
}
