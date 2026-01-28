package com.kafkaadmin.user;

import java.util.List;

/**
 * Domain model representing SCRAM credentials for a user.
 *
 * @param name user name
 * @param credentialInfos list of SCRAM credential info
 */
public record UserScramCredential(
        String name,
        List<ScramCredentialInfo> credentialInfos
) {
}
