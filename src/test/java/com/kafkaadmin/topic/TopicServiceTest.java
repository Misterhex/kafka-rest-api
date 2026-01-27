package com.kafkaadmin.topic;

import com.kafkaadmin.common.KafkaAdminPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private KafkaAdminPort kafkaAdminPort;

    private TopicService topicService;

    @BeforeEach
    void setUp() {
        topicService = new TopicService(kafkaAdminPort);
    }

    @Test
    void listTopics_shouldReturnSortedTopics() {
        // Given
        Topic topic1 = createTopic("zebra-topic");
        Topic topic2 = createTopic("alpha-topic");

        when(kafkaAdminPort.listTopicNames()).thenReturn(List.of("zebra-topic", "alpha-topic"));
        when(kafkaAdminPort.getTopic("zebra-topic")).thenReturn(topic1);
        when(kafkaAdminPort.getTopic("alpha-topic")).thenReturn(topic2);

        // When
        List<TopicResponse> result = topicService.listTopics();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("alpha-topic");
        assertThat(result.get(1).name()).isEqualTo("zebra-topic");
    }

    @Test
    void getTopic_shouldReturnTopicDetails() {
        // Given
        String topicName = "test-topic";
        Topic topic = new Topic(
                topicName,
                3,
                2,
                Map.of("retention.ms", "86400000"),
                List.of(
                        new TopicPartitionInfo(0, 1, List.of(1, 2), List.of(1, 2)),
                        new TopicPartitionInfo(1, 2, List.of(2, 1), List.of(2, 1)),
                        new TopicPartitionInfo(2, 1, List.of(1, 2), List.of(1, 2))
                ),
                false);

        when(kafkaAdminPort.getTopic(topicName)).thenReturn(topic);

        // When
        TopicDetailResponse result = topicService.getTopic(topicName);

        // Then
        assertThat(result.name()).isEqualTo(topicName);
        assertThat(result.partitionCount()).isEqualTo(3);
        assertThat(result.replicationFactor()).isEqualTo(2);
        assertThat(result.configs()).containsEntry("retention.ms", "86400000");
        assertThat(result.partitions()).hasSize(3);
    }

    @Test
    void getTopic_whenNotFound_shouldThrowException() {
        // Given
        String topicName = "non-existent";
        when(kafkaAdminPort.getTopic(topicName)).thenThrow(new TopicNotFoundException(topicName));

        // When/Then
        assertThatThrownBy(() -> topicService.getTopic(topicName))
                .isInstanceOf(TopicNotFoundException.class)
                .hasMessageContaining(topicName);
    }

    @Test
    void getTopicPartitions_shouldReturnSortedPartitions() {
        // Given
        String topicName = "test-topic";
        List<TopicPartitionInfo> partitions = List.of(
                new TopicPartitionInfo(2, 1, List.of(1, 2), List.of(1, 2)),
                new TopicPartitionInfo(0, 1, List.of(1, 2), List.of(1, 2)),
                new TopicPartitionInfo(1, 2, List.of(2, 1), List.of(2, 1))
        );

        when(kafkaAdminPort.getTopicPartitions(topicName)).thenReturn(partitions);

        // When
        List<TopicPartitionInfoResponse> result = topicService.getTopicPartitions(topicName);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).partition()).isEqualTo(0);
        assertThat(result.get(1).partition()).isEqualTo(1);
        assertThat(result.get(2).partition()).isEqualTo(2);
    }

    private Topic createTopic(String name) {
        return new Topic(
                name,
                1,
                1,
                Map.of(),
                List.of(new TopicPartitionInfo(0, 1, List.of(1), List.of(1))),
                false);
    }
}
