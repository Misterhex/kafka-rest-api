package com.kafkaadmin.consumergroup;

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
 * REST controller for Kafka consumer group operations.
 *
 * <p>Provides endpoints for listing consumer groups, retrieving details, and checking offsets.
 */
@RestController
@RequestMapping("/api/v1/consumer-groups")
@Tag(name = "Consumer Groups", description = "Kafka consumer group management operations")
public class ConsumerGroupController {

    private final ConsumerGroupService consumerGroupService;

    /**
     * Creates a controller with the given service.
     *
     * @param consumerGroupService the consumer group service
     */
    public ConsumerGroupController(ConsumerGroupService consumerGroupService) {
        this.consumerGroupService = consumerGroupService;
    }

    /**
     * Lists all consumer groups.
     *
     * @return list of consumer groups sorted by group ID
     */
    @GetMapping
    @Operation(summary = "List all consumer groups", description = "Returns a list of all Kafka consumer groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved consumer groups"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ConsumerGroupResponse>> listConsumerGroups() {
        return ResponseEntity.ok(consumerGroupService.listConsumerGroups());
    }

    /**
     * Retrieves details for a specific consumer group.
     *
     * @param groupId the consumer group ID
     * @return consumer group details including members
     */
    @GetMapping("/{groupId}")
    @Operation(summary = "Get consumer group details",
            description = "Returns detailed information about a specific consumer group including members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved consumer group details"),
            @ApiResponse(responseCode = "404", description = "Consumer group not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ConsumerGroupDetailResponse> getConsumerGroup(
            @Parameter(description = "Consumer group ID", required = true)
            @PathVariable String groupId) {
        return ResponseEntity.ok(consumerGroupService.getConsumerGroup(groupId));
    }

    /**
     * Retrieves offset information for a consumer group.
     *
     * @param groupId the consumer group ID
     * @return list of offset details per topic-partition
     */
    @GetMapping("/{groupId}/offsets")
    @Operation(summary = "Get consumer group offsets",
            description = "Returns offset information and lag for a consumer group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved consumer group offsets"),
            @ApiResponse(responseCode = "404", description = "Consumer group not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ConsumerGroupOffsetResponse>> getConsumerGroupOffsets(
            @Parameter(description = "Consumer group ID", required = true)
            @PathVariable String groupId) {
        return ResponseEntity.ok(consumerGroupService.getConsumerGroupOffsets(groupId));
    }
}
