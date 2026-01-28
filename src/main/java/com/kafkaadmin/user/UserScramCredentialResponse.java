package com.kafkaadmin.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing SCRAM credentials for a user.
 *
 * @param name user name
 * @param credentialInfos list of SCRAM credential info
 */
@Schema(description = "User SCRAM credentials")
public record UserScramCredentialResponse(
        @Schema(description = "User name", example = "alice")
        String name,

        @Schema(description = "SCRAM credential information")
        List<ScramCredentialInfoResponse> credentialInfos
) {
    public static UserScramCredentialResponse from(UserScramCredential credential) {
        return new UserScramCredentialResponse(
                credential.name(),
                credential.credentialInfos().stream()
                        .map(ScramCredentialInfoResponse::from)
                        .toList()
        );
    }
}
