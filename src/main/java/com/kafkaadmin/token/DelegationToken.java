package com.kafkaadmin.token;

import java.util.List;

/**
 * Domain model representing a delegation token.
 *
 * @param tokenId token ID
 * @param owner owner principal
 * @param tokenRequester requester principal
 * @param renewers list of renewer principals
 * @param issueTimestamp issue timestamp in milliseconds
 * @param expiryTimestamp expiry timestamp in milliseconds
 * @param maxTimestamp max timestamp in milliseconds
 */
public record DelegationToken(
        String tokenId,
        String owner,
        String tokenRequester,
        List<String> renewers,
        long issueTimestamp,
        long expiryTimestamp,
        long maxTimestamp
) {
}
