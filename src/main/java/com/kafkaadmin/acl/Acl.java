package com.kafkaadmin.acl;

/**
 * Domain model representing an Access Control List entry.
 *
 * @param resourceType type of resource (TOPIC, GROUP, CLUSTER, etc.)
 * @param resourceName name of the resource
 * @param patternType pattern type (LITERAL, PREFIXED, etc.)
 * @param principal principal (user or group)
 * @param host host from which access is allowed
 * @param operation operation type (READ, WRITE, ALL, etc.)
 * @param permissionType permission type (ALLOW, DENY)
 */
public record Acl(
        String resourceType,
        String resourceName,
        String patternType,
        String principal,
        String host,
        String operation,
        String permissionType
) {
}
