package com.kafkaadmin.quota;

import java.util.Map;

/**
 * Domain model representing a client quota configuration.
 *
 * @param entityType entity type (USER, CLIENT_ID, IP)
 * @param entityName entity name (or null for default)
 * @param quotas quota key-value pairs
 */
public record ClientQuota(
        String entityType,
        String entityName,
        Map<String, Double> quotas
) {
}
