package com.kafkaadmin.token;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response representing a delegation token.
 *
 * @param tokenId token ID
 * @param owner owner principal
 * @param tokenRequester requester principal
 * @param renewers list of renewer principals
 * @param issueTimestamp issue timestamp in ms
 * @param expiryTimestamp expiry timestamp in ms
 * @param maxTimestamp max timestamp in ms
 */
@Schema(description = "Delegation token")
public record DelegationTokenResponse(
        @Schema(description = "Token ID", example = "abc123")
        String tokenId,

        @Schema(description = "Owner principal", example = "User:admin")
        String owner,

        @Schema(description = "Token requester principal", example = "User:admin")
        String tokenRequester,

        @Schema(description = "Renewer principals")
        List<String> renewers,

        @Schema(description = "Issue timestamp in milliseconds", example = "1704067200000")
        long issueTimestamp,

        @Schema(description = "Expiry timestamp in milliseconds", example = "1704153600000")
        long expiryTimestamp,

        @Schema(description = "Max timestamp in milliseconds", example = "1704672000000")
        long maxTimestamp
) {
    public static DelegationTokenResponse from(DelegationToken token) {
        return new DelegationTokenResponse(
                token.tokenId(),
                token.owner(),
                token.tokenRequester(),
                token.renewers(),
                token.issueTimestamp(),
                token.expiryTimestamp(),
                token.maxTimestamp()
        );
    }
}
