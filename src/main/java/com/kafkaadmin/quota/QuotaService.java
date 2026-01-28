package com.kafkaadmin.quota;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Service providing client quota operations.
 */
@Service
public class QuotaService {

    private final KafkaAdminPort kafkaAdminPort;

    public QuotaService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    /**
     * Lists all client quotas in the cluster.
     *
     * @return list of client quota responses sorted by entity type and name
     */
    public List<ClientQuotaResponse> listQuotas() {
        return kafkaAdminPort.describeClientQuotas().stream()
                .map(ClientQuotaResponse::from)
                .sorted(Comparator.comparing(ClientQuotaResponse::entityType)
                        .thenComparing(q -> q.entityName() != null ? q.entityName() : ""))
                .toList();
    }
}
