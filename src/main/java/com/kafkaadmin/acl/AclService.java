package com.kafkaadmin.acl;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Service providing ACL operations.
 */
@Service
public class AclService {

    private final KafkaAdminPort kafkaAdminPort;

    public AclService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    /**
     * Lists all ACLs in the cluster.
     *
     * @return list of ACL responses sorted by resource type, resource name, and principal
     */
    public List<AclResponse> listAcls() {
        return kafkaAdminPort.describeAcls().stream()
                .map(AclResponse::from)
                .sorted(Comparator.comparing(AclResponse::resourceType)
                        .thenComparing(AclResponse::resourceName)
                        .thenComparing(AclResponse::principal))
                .toList();
    }
}
