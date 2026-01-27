package com.kafkaadmin.consumergroup;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ConsumerGroupService {

    private final KafkaAdminPort kafkaAdminPort;

    public ConsumerGroupService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    public List<ConsumerGroupDto> listConsumerGroups() {
        return kafkaAdminPort.listConsumerGroupIds().stream()
                .map(kafkaAdminPort::getConsumerGroup)
                .map(ConsumerGroupDto::from)
                .sorted(Comparator.comparing(ConsumerGroupDto::groupId))
                .toList();
    }

    public ConsumerGroupDetailDto getConsumerGroup(String groupId) {
        ConsumerGroup group = kafkaAdminPort.getConsumerGroup(groupId);
        return ConsumerGroupDetailDto.from(group);
    }

    public List<ConsumerGroupOffsetDto> getConsumerGroupOffsets(String groupId) {
        return kafkaAdminPort.getConsumerGroupOffsets(groupId).stream()
                .map(ConsumerGroupOffsetDto::from)
                .sorted(Comparator.comparing(ConsumerGroupOffsetDto::topic)
                        .thenComparingInt(ConsumerGroupOffsetDto::partition))
                .toList();
    }
}
