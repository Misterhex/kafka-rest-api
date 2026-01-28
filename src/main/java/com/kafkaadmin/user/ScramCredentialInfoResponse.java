package com.kafkaadmin.user;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing a single SCRAM credential.
 *
 * @param mechanism SCRAM mechanism
 * @param iterations iteration count
 */
@Schema(description = "SCRAM credential information")
public record ScramCredentialInfoResponse(
        @Schema(description = "SCRAM mechanism", example = "SCRAM-SHA-256")
        String mechanism,

        @Schema(description = "Iteration count", example = "4096")
        int iterations
) {
    public static ScramCredentialInfoResponse from(ScramCredentialInfo info) {
        return new ScramCredentialInfoResponse(info.mechanism(), info.iterations());
    }
}
