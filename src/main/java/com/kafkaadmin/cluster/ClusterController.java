package com.kafkaadmin.cluster;

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
 * REST controller for Kafka cluster operations.
 *
 * <p>Provides endpoints for retrieving cluster information and broker details.
 */
@RestController
@RequestMapping("/api/v1/cluster")
@Tag(name = "Cluster", description = "Kafka cluster management operations")
public class ClusterController {

    private final ClusterService clusterService;

    /**
     * Creates a controller with the given service.
     *
     * @param clusterService the cluster service
     */
    public ClusterController(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    /**
     * Retrieves cluster information.
     *
     * @return cluster information including controller and brokers
     */
    @GetMapping
    @Operation(summary = "Get cluster info",
            description = "Returns information about the Kafka cluster including controller and brokers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cluster info"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ClusterInfoResponse> getClusterInfo() {
        return ResponseEntity.ok(clusterService.getClusterInfo());
    }

    /**
     * Lists all brokers in the cluster.
     *
     * @return list of brokers sorted by ID
     */
    @GetMapping("/brokers")
    @Operation(summary = "List all brokers", description = "Returns a list of all Kafka brokers in the cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved brokers"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<BrokerResponse>> listBrokers() {
        return ResponseEntity.ok(clusterService.listBrokers());
    }

    /**
     * Retrieves details for a specific broker.
     *
     * @param id the broker ID
     * @return broker details including configuration
     */
    @GetMapping("/brokers/{id}")
    @Operation(summary = "Get broker details",
            description = "Returns detailed information about a specific broker including configuration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved broker details"),
            @ApiResponse(responseCode = "404", description = "Broker not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BrokerDetailResponse> getBroker(
            @Parameter(description = "Broker ID", required = true)
            @PathVariable int id) {
        return ResponseEntity.ok(clusterService.getBroker(id));
    }

    /**
     * Describes log directories for a specific broker.
     *
     * @param id the broker ID
     * @return list of log directory information
     */
    @GetMapping("/brokers/{id}/log-dirs")
    @Operation(summary = "Get broker log directories",
            description = "Returns log directory information for a specific broker")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved log directories"),
            @ApiResponse(responseCode = "404", description = "Broker not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<LogDirInfoResponse>> getBrokerLogDirs(
            @Parameter(description = "Broker ID", required = true)
            @PathVariable int id) {
        return ResponseEntity.ok(clusterService.getBrokerLogDirs(id));
    }

    /**
     * Lists ongoing partition reassignments.
     *
     * @return list of partition reassignments
     */
    @GetMapping("/reassignments")
    @Operation(summary = "List partition reassignments",
            description = "Returns a list of ongoing partition reassignments in the cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reassignments"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<PartitionReassignmentResponse>> listPartitionReassignments() {
        return ResponseEntity.ok(clusterService.listPartitionReassignments());
    }

    /**
     * Describes Kafka features.
     *
     * @return list of Kafka features
     */
    @GetMapping("/features")
    @Operation(summary = "List Kafka features",
            description = "Returns a list of Kafka features and their version information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved features"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<KafkaFeatureResponse>> describeFeatures() {
        return ResponseEntity.ok(clusterService.describeFeatures());
    }

    /**
     * Describes the metadata quorum (KRaft mode).
     *
     * @return quorum information
     */
    @GetMapping("/quorum")
    @Operation(summary = "Get metadata quorum info",
            description = "Returns metadata quorum information (only available in KRaft mode)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved quorum info"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<QuorumInfoResponse> describeMetadataQuorum() {
        return ResponseEntity.ok(clusterService.describeMetadataQuorum());
    }
}
