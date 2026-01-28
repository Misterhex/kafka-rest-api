package com.kafkaadmin.acl;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing an ACL entry.
 *
 * @param resourceType type of resource
 * @param resourceName name of the resource
 * @param patternType pattern type
 * @param principal principal
 * @param host host
 * @param operation operation type
 * @param permissionType permission type
 */
@Schema(description = "Access Control List entry")
public record AclResponse(
        @Schema(description = "Resource type", example = "TOPIC")
        String resourceType,

        @Schema(description = "Resource name", example = "my-topic")
        String resourceName,

        @Schema(description = "Pattern type", example = "LITERAL")
        String patternType,

        @Schema(description = "Principal", example = "User:alice")
        String principal,

        @Schema(description = "Host", example = "*")
        String host,

        @Schema(description = "Operation", example = "READ")
        String operation,

        @Schema(description = "Permission type", example = "ALLOW")
        String permissionType
) {
    public static AclResponse from(Acl acl) {
        return new AclResponse(
                acl.resourceType(),
                acl.resourceName(),
                acl.patternType(),
                acl.principal(),
                acl.host(),
                acl.operation(),
                acl.permissionType()
        );
    }
}
