package com.kafkaadmin.sharegroup;

import com.kafkaadmin.common.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Kafka share group operations.
 *
 * <p>Provides endpoints for listing share groups and retrieving details.
 */
@RestController
@RequestMapping("/api/v1/share-groups")
@Tag(name = "Share Groups", description = "Kafka share group management operations")
public class ShareGroupController {

    private final ShareGroupService shareGroupService;

    /**
     * Creates a controller with the given service.
     *
     * @param shareGroupService the share group service
     */
    public ShareGroupController(ShareGroupService shareGroupService) {
        this.shareGroupService = shareGroupService;
    }

    /**
     * Lists all share groups.
     *
     * @return list of share groups sorted by group ID
     */
    @GetMapping
    @Operation(summary = "List all share groups", description = "Returns a list of all Kafka share groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved share groups"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ShareGroupResponse>> listShareGroups() {
        return ResponseEntity.ok(shareGroupService.listShareGroups());
    }

    /**
     * Retrieves details for a specific share group.
     *
     * @param groupId the share group ID
     * @return share group details including members
     */
    @GetMapping("/{groupId}")
    @Operation(summary = "Get share group details",
            description = "Returns detailed information about a specific share group including members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved share group details"),
            @ApiResponse(responseCode = "404", description = "Share group not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ShareGroupDetailResponse> getShareGroup(
            @Parameter(description = "Share group ID", required = true)
            @PathVariable String groupId) {
        return ResponseEntity.ok(shareGroupService.getShareGroup(groupId));
    }
}
