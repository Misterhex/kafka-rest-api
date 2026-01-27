package com.kafkaadmin.topic;

import com.kafkaadmin.common.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TopicControllerTest {

    @Mock
    private TopicService topicService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TopicController controller = new TopicController(topicService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listTopics_shouldReturnTopics() throws Exception {
        // Given
        List<TopicResponse> topics = List.of(
                new TopicResponse("topic-1", 3, 2, false),
                new TopicResponse("topic-2", 1, 1, false)
        );
        when(topicService.listTopics()).thenReturn(topics);

        // When/Then
        mockMvc.perform(get("/api/v1/topics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("topic-1"))
                .andExpect(jsonPath("$[0].partitionCount").value(3))
                .andExpect(jsonPath("$[1].name").value("topic-2"));
    }

    @Test
    void getTopic_shouldReturnTopicDetails() throws Exception {
        // Given
        TopicDetailResponse topic = new TopicDetailResponse(
                "my-topic",
                3,
                2,
                false,
                Map.of("retention.ms", "86400000"),
                List.of(
                        new TopicPartitionInfoResponse(0, 1, List.of(1, 2), List.of(1, 2)),
                        new TopicPartitionInfoResponse(1, 2, List.of(2, 1), List.of(2, 1))
                )
        );
        when(topicService.getTopic("my-topic")).thenReturn(topic);

        // When/Then
        mockMvc.perform(get("/api/v1/topics/my-topic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("my-topic"))
                .andExpect(jsonPath("$.partitionCount").value(3))
                .andExpect(jsonPath("$.replicationFactor").value(2))
                .andExpect(jsonPath("$.configs.['retention.ms']").value("86400000"))
                .andExpect(jsonPath("$.partitions.length()").value(2));
    }

    @Test
    void getTopic_whenNotFound_shouldReturn404() throws Exception {
        // Given
        when(topicService.getTopic("non-existent"))
                .thenThrow(new TopicNotFoundException("non-existent"));

        // When/Then
        mockMvc.perform(get("/api/v1/topics/non-existent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Topic 'non-existent' not found"));
    }

    @Test
    void getTopicPartitions_shouldReturnPartitions() throws Exception {
        // Given
        List<TopicPartitionInfoResponse> partitions = List.of(
                new TopicPartitionInfoResponse(0, 1, List.of(1, 2), List.of(1, 2)),
                new TopicPartitionInfoResponse(1, 2, List.of(2, 1), List.of(2, 1))
        );
        when(topicService.getTopicPartitions("my-topic")).thenReturn(partitions);

        // When/Then
        mockMvc.perform(get("/api/v1/topics/my-topic/partitions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].partition").value(0))
                .andExpect(jsonPath("$[0].leader").value(1));
    }
}
