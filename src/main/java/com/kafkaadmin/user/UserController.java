package com.kafkaadmin.user;

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
 * REST controller for user credential operations.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Kafka user credential operations")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Lists all user SCRAM credentials in the cluster.
     *
     * @return list of user SCRAM credentials sorted by user name
     */
    @GetMapping("/credentials")
    @Operation(summary = "List all user SCRAM credentials",
            description = "Returns a list of all user SCRAM credentials in the Kafka cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<UserScramCredentialResponse>> listScramCredentials() {
        return ResponseEntity.ok(userService.listScramCredentials());
    }
}
