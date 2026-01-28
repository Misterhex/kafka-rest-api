package com.kafkaadmin.user;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Service providing user credential operations.
 */
@Service
public class UserService {

    private final KafkaAdminPort kafkaAdminPort;

    public UserService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    /**
     * Lists all user SCRAM credentials in the cluster.
     *
     * @return list of user SCRAM credential responses sorted by user name
     */
    public List<UserScramCredentialResponse> listScramCredentials() {
        return kafkaAdminPort.describeUserScramCredentials().stream()
                .map(UserScramCredentialResponse::from)
                .sorted(Comparator.comparing(UserScramCredentialResponse::name))
                .toList();
    }
}
