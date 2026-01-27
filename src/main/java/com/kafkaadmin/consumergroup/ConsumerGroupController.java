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

@RestController
@RequestMapping("/api/v1/consumer-groups")
@Tag(name = "Consumer Groups", description = "Kafka consumer group management operations")
public class ConsumerGroupController {

    private final ConsumerGroupService consumerGroupService;

    public ConsumerGroupController(ConsumerGroupService consumerGroupService) {
        this.consumerGroupService = consumerGroupService;
    }

    @GetMapping
    @Operation(summary = "List all consumer groups", description = "Returns a list of all Kafka consumer groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved consumer groups"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ConsumerGroupDto>> listConsumerGroups() {
        return ResponseEntity.ok(consumerGroupService.listConsumerGroups());
    }

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
    public ResponseEntity<ConsumerGroupDetailDto> getConsumerGroup(
            @Parameter(description = "Consumer group ID", required = true)
            @PathVariable String groupId) {
        return ResponseEntity.ok(consumerGroupService.getConsumerGroup(groupId));
    }

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
    public ResponseEntity<List<ConsumerGroupOffsetDto>> getConsumerGroupOffsets(
            @Parameter(description = "Consumer group ID", required = true)
            @PathVariable String groupId) {
        return ResponseEntity.ok(consumerGroupService.getConsumerGroupOffsets(groupId));
    }
}
