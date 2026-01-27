package com.kafkaadmin.topic;

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
 * REST controller for Kafka topic operations.
 *
 * <p>Provides endpoints for listing topics and retrieving topic details.
 */
@RestController
@RequestMapping("/api/v1/topics")
@Tag(name = "Topics", description = "Kafka topic management operations")
public class TopicController {

    private final TopicService topicService;

    /**
     * Creates a controller with the given service.
     *
     * @param topicService the topic service
     */
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Lists all topics.
     *
     * @return list of topics sorted by name
     */
    @GetMapping
    @Operation(summary = "List all topics", description = "Returns a list of all Kafka topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved topics"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TopicResponse>> listTopics() {
        return ResponseEntity.ok(topicService.listTopics());
    }

    /**
     * Retrieves details for a specific topic.
     *
     * @param name the topic name
     * @return topic details including configuration and partitions
     */
    @GetMapping("/{name}")
    @Operation(summary = "Get topic details", description = "Returns detailed information about a specific topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved topic details"),
            @ApiResponse(responseCode = "404", description = "Topic not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TopicDetailResponse> getTopic(
            @Parameter(description = "Topic name", required = true)
            @PathVariable String name) {
        return ResponseEntity.ok(topicService.getTopic(name));
    }

    /**
     * Retrieves partition information for a topic.
     *
     * @param name the topic name
     * @return list of partition details sorted by partition number
     */
    @GetMapping("/{name}/partitions")
    @Operation(summary = "Get topic partitions", description = "Returns partition information for a specific topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved partition details"),
            @ApiResponse(responseCode = "404", description = "Topic not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TopicPartitionInfoResponse>> getTopicPartitions(
            @Parameter(description = "Topic name", required = true)
            @PathVariable String name) {
        return ResponseEntity.ok(topicService.getTopicPartitions(name));
    }
}
