package com.kafkaadmin.sharegroup;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Service providing share group management operations.
 */
@Service
public class ShareGroupService {

    private final KafkaAdminPort kafkaAdminPort;

    /**
     * Creates a service with the given Kafka admin port.
     *
     * @param kafkaAdminPort the Kafka admin port
     */
    public ShareGroupService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    /**
     * Lists all share groups.
     *
     * @return list of share group DTOs sorted by group ID
     */
    public List<ShareGroupResponse> listShareGroups() {
        return kafkaAdminPort.listShareGroupIds().stream()
                .map(kafkaAdminPort::describeShareGroup)
                .map(ShareGroupResponse::from)
                .sorted(Comparator.comparing(ShareGroupResponse::groupId))
                .toList();
    }

    /**
     * Retrieves details for a specific share group.
     *
     * @param groupId the share group ID
     * @return share group detail DTO
     * @throws ShareGroupNotFoundException if the group does not exist
     */
    public ShareGroupDetailResponse getShareGroup(String groupId) {
        ShareGroup group = kafkaAdminPort.describeShareGroup(groupId);
        return ShareGroupDetailResponse.from(group);
    }
}
