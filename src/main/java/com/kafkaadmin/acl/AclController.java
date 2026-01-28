package com.kafkaadmin.acl;

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
 * REST controller for ACL operations.
 */
@RestController
@RequestMapping("/api/v1/acls")
@Tag(name = "ACLs", description = "Kafka Access Control List operations")
public class AclController {

    private final AclService aclService;

    public AclController(AclService aclService) {
        this.aclService = aclService;
    }

    /**
     * Lists all ACLs in the cluster.
     *
     * @return list of ACLs sorted by resource type, name, and principal
     */
    @GetMapping
    @Operation(summary = "List all ACLs",
            description = "Returns a list of all access control entries in the Kafka cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved ACLs"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<AclResponse>> listAcls() {
        return ResponseEntity.ok(aclService.listAcls());
    }
}
