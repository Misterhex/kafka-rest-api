package com.kafkaadmin.token;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Service providing delegation token operations.
 */
@Service
public class TokenService {

    private final KafkaAdminPort kafkaAdminPort;

    public TokenService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    /**
     * Lists all delegation tokens in the cluster.
     *
     * @return list of delegation token responses sorted by token ID
     */
    public List<DelegationTokenResponse> listTokens() {
        return kafkaAdminPort.describeDelegationTokens().stream()
                .map(DelegationTokenResponse::from)
                .sorted(Comparator.comparing(DelegationTokenResponse::tokenId))
                .toList();
    }
}
