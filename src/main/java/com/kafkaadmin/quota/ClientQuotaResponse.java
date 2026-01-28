package com.kafkaadmin.quota;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * Response representing a client quota configuration.
 *
 * @param entityType entity type
 * @param entityName entity name (null for default)
 * @param quotas quota key-value pairs
 */
@Schema(description = "Client quota configuration")
public record ClientQuotaResponse(
        @Schema(description = "Entity type", example = "USER")
        String entityType,

        @Schema(description = "Entity name (null for default)", example = "alice")
        String entityName,

        @Schema(description = "Quota key-value pairs")
        Map<String, Double> quotas
) {
    public static ClientQuotaResponse from(ClientQuota quota) {
        return new ClientQuotaResponse(
                quota.entityType(),
                quota.entityName(),
                quota.quotas()
        );
    }
}
