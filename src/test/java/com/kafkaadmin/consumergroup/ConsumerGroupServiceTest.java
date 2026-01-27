package com.kafkaadmin.consumergroup;

import com.kafkaadmin.common.KafkaAdminPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsumerGroupServiceTest {

    @Mock
    private KafkaAdminPort kafkaAdminPort;

    private ConsumerGroupService consumerGroupService;

    @BeforeEach
    void setUp() {
        consumerGroupService = new ConsumerGroupService(kafkaAdminPort);
    }

    @Test
    void listConsumerGroups_shouldReturnSortedGroups() {
        // Given
        ConsumerGroup group1 = createConsumerGroup("zebra-group");
        ConsumerGroup group2 = createConsumerGroup("alpha-group");

        when(kafkaAdminPort.listConsumerGroupIds()).thenReturn(List.of("zebra-group", "alpha-group"));
        when(kafkaAdminPort.getConsumerGroup("zebra-group")).thenReturn(group1);
        when(kafkaAdminPort.getConsumerGroup("alpha-group")).thenReturn(group2);

        // When
        List<ConsumerGroupResponse> result = consumerGroupService.listConsumerGroups();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).groupId()).isEqualTo("alpha-group");
        assertThat(result.get(1).groupId()).isEqualTo("zebra-group");
    }

    @Test
    void getConsumerGroup_shouldReturnGroupDetails() {
        // Given
        String groupId = "test-group";
        ConsumerGroup group = new ConsumerGroup(
                groupId,
                "Stable",
                "range",
                1,
                List.of(
                        new ConsumerGroupMember("consumer-1-id", "consumer-1", "/192.168.1.100",
                                List.of("topic-0", "topic-1")),
                        new ConsumerGroupMember("consumer-2-id", "consumer-2", "/192.168.1.101",
                                List.of("topic-2"))
                ));

        when(kafkaAdminPort.getConsumerGroup(groupId)).thenReturn(group);

        // When
        ConsumerGroupDetailResponse result = consumerGroupService.getConsumerGroup(groupId);

        // Then
        assertThat(result.groupId()).isEqualTo(groupId);
        assertThat(result.state()).isEqualTo("Stable");
        assertThat(result.partitionAssignor()).isEqualTo("range");
        assertThat(result.coordinatorId()).isEqualTo(1);
        assertThat(result.members()).hasSize(2);
    }

    @Test
    void getConsumerGroup_whenNotFound_shouldThrowException() {
        // Given
        String groupId = "non-existent";
        when(kafkaAdminPort.getConsumerGroup(groupId))
                .thenThrow(new ConsumerGroupNotFoundException(groupId));

        // When/Then
        assertThatThrownBy(() -> consumerGroupService.getConsumerGroup(groupId))
                .isInstanceOf(ConsumerGroupNotFoundException.class)
                .hasMessageContaining(groupId);
    }

    @Test
    void getConsumerGroupOffsets_shouldReturnSortedOffsets() {
        // Given
        String groupId = "test-group";
        List<ConsumerGroupOffset> offsets = List.of(
                new ConsumerGroupOffset("topic-b", 0, 100, 150, 50),
                new ConsumerGroupOffset("topic-a", 1, 200, 250, 50),
                new ConsumerGroupOffset("topic-a", 0, 100, 120, 20)
        );

        when(kafkaAdminPort.getConsumerGroupOffsets(groupId)).thenReturn(offsets);

        // When
        List<ConsumerGroupOffsetResponse> result = consumerGroupService.getConsumerGroupOffsets(groupId);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).topic()).isEqualTo("topic-a");
        assertThat(result.get(0).partition()).isEqualTo(0);
        assertThat(result.get(1).topic()).isEqualTo("topic-a");
        assertThat(result.get(1).partition()).isEqualTo(1);
        assertThat(result.get(2).topic()).isEqualTo("topic-b");
    }

    private ConsumerGroup createConsumerGroup(String groupId) {
        return new ConsumerGroup(
                groupId,
                "Stable",
                "range",
                1,
                List.of());
    }
}
