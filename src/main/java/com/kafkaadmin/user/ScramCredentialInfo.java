package com.kafkaadmin.user;

/**
 * Domain model representing a single SCRAM credential.
 *
 * @param mechanism SCRAM mechanism (SCRAM-SHA-256, SCRAM-SHA-512)
 * @param iterations iteration count
 */
public record ScramCredentialInfo(
        String mechanism,
        int iterations
) {
}
