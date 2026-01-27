package com.kafkaadmin.consumergroup;

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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ConsumerGroupControllerTest {

    @Mock
    private ConsumerGroupService consumerGroupService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ConsumerGroupController controller = new ConsumerGroupController(consumerGroupService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listConsumerGroups_shouldReturnGroups() throws Exception {
        // Given
        List<ConsumerGroupResponse> groups = List.of(
                new ConsumerGroupResponse("group-1", "Stable", 2),
                new ConsumerGroupResponse("group-2", "Empty", 0)
        );
        when(consumerGroupService.listConsumerGroups()).thenReturn(groups);

        // When/Then
        mockMvc.perform(get("/api/v1/consumer-groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].groupId").value("group-1"))
                .andExpect(jsonPath("$[0].state").value("Stable"))
                .andExpect(jsonPath("$[0].memberCount").value(2));
    }

    @Test
    void getConsumerGroup_shouldReturnGroupDetails() throws Exception {
        // Given
        ConsumerGroupDetailResponse group = new ConsumerGroupDetailResponse(
                "my-group",
                "Stable",
                "range",
                1,
                List.of(
                        new ConsumerGroupMemberResponse("consumer-1-id", "consumer-1", "/192.168.1.100",
                                List.of("topic-0"))
                )
        );
        when(consumerGroupService.getConsumerGroup("my-group")).thenReturn(group);

        // When/Then
        mockMvc.perform(get("/api/v1/consumer-groups/my-group")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value("my-group"))
                .andExpect(jsonPath("$.state").value("Stable"))
                .andExpect(jsonPath("$.partitionAssignor").value("range"))
                .andExpect(jsonPath("$.members.length()").value(1));
    }

    @Test
    void getConsumerGroup_whenNotFound_shouldReturn404() throws Exception {
        // Given
        when(consumerGroupService.getConsumerGroup("non-existent"))
                .thenThrow(new ConsumerGroupNotFoundException("non-existent"));

        // When/Then
        mockMvc.perform(get("/api/v1/consumer-groups/non-existent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Consumer group 'non-existent' not found"));
    }

    @Test
    void getConsumerGroupOffsets_shouldReturnOffsets() throws Exception {
        // Given
        List<ConsumerGroupOffsetResponse> offsets = List.of(
                new ConsumerGroupOffsetResponse("topic-1", 0, 100, 150, 50),
                new ConsumerGroupOffsetResponse("topic-1", 1, 200, 250, 50)
        );
        when(consumerGroupService.getConsumerGroupOffsets("my-group")).thenReturn(offsets);

        // When/Then
        mockMvc.perform(get("/api/v1/consumer-groups/my-group/offsets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].topic").value("topic-1"))
                .andExpect(jsonPath("$[0].partition").value(0))
                .andExpect(jsonPath("$[0].currentOffset").value(100))
                .andExpect(jsonPath("$[0].endOffset").value(150))
                .andExpect(jsonPath("$[0].lag").value(50));
    }
}
