package com.kafkaadmin.consumergroup;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Service providing consumer group management operations.
 */
@Service
public class ConsumerGroupService {

    private final KafkaAdminPort kafkaAdminPort;

    /**
     * Creates a service with the given Kafka admin port.
     *
     * @param kafkaAdminPort the Kafka admin port
     */
    public ConsumerGroupService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    /**
     * Lists all consumer groups.
     *
     * @return list of consumer group DTOs sorted by group ID
     */
    public List<ConsumerGroupResponse> listConsumerGroups() {
        return kafkaAdminPort.listConsumerGroupIds().stream()
                .map(kafkaAdminPort::getConsumerGroup)
                .map(ConsumerGroupResponse::from)
                .sorted(Comparator.comparing(ConsumerGroupResponse::groupId))
                .toList();
    }

    /**
     * Retrieves details for a specific consumer group.
     *
     * @param groupId the consumer group ID
     * @return consumer group detail DTO
     * @throws ConsumerGroupNotFoundException if the group does not exist
     */
    public ConsumerGroupDetailResponse getConsumerGroup(String groupId) {
        ConsumerGroup group = kafkaAdminPort.getConsumerGroup(groupId);
        return ConsumerGroupDetailResponse.from(group);
    }

    /**
     * Retrieves offset information for a consumer group.
     *
     * @param groupId the consumer group ID
     * @return list of offset DTOs sorted by topic and partition
     * @throws ConsumerGroupNotFoundException if the group does not exist
     */
    public List<ConsumerGroupOffsetResponse> getConsumerGroupOffsets(String groupId) {
        return kafkaAdminPort.getConsumerGroupOffsets(groupId).stream()
                .map(ConsumerGroupOffsetResponse::from)
                .sorted(Comparator.comparing(ConsumerGroupOffsetResponse::topic)
                        .thenComparingInt(ConsumerGroupOffsetResponse::partition))
                .toList();
    }
}
