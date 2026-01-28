package com.kafkaadmin.sharegroup;

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
class ShareGroupControllerTest {

    @Mock
    private ShareGroupService shareGroupService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ShareGroupController controller = new ShareGroupController(shareGroupService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listShareGroups_shouldReturnShareGroups() throws Exception {
        // Given
        List<ShareGroupResponse> groups = List.of(
                new ShareGroupResponse("group-1", "Stable", 2),
                new ShareGroupResponse("group-2", "Empty", 0)
        );
        when(shareGroupService.listShareGroups()).thenReturn(groups);

        // When/Then
        mockMvc.perform(get("/api/v1/share-groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].groupId").value("group-1"))
                .andExpect(jsonPath("$[0].state").value("Stable"))
                .andExpect(jsonPath("$[0].memberCount").value(2));
    }

    @Test
    void listShareGroups_whenEmpty_shouldReturnEmptyArray() throws Exception {
        // Given
        when(shareGroupService.listShareGroups()).thenReturn(List.of());

        // When/Then
        mockMvc.perform(get("/api/v1/share-groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getShareGroup_shouldReturnShareGroupDetail() throws Exception {
        // Given
        ShareGroupDetailResponse group = new ShareGroupDetailResponse(
                "my-share-group",
                "Stable",
                1,
                List.of(
                        new ShareGroupMemberResponse("consumer-1-id", "consumer-1", "/192.168.1.100",
                                List.of("topic-0"))
                )
        );
        when(shareGroupService.getShareGroup("my-share-group")).thenReturn(group);

        // When/Then
        mockMvc.perform(get("/api/v1/share-groups/my-share-group")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value("my-share-group"))
                .andExpect(jsonPath("$.state").value("Stable"))
                .andExpect(jsonPath("$.coordinatorId").value(1))
                .andExpect(jsonPath("$.members.length()").value(1));
    }

    @Test
    void getShareGroup_whenNotFound_shouldReturn404() throws Exception {
        // Given
        when(shareGroupService.getShareGroup("non-existent"))
                .thenThrow(new ShareGroupNotFoundException("non-existent"));

        // When/Then
        mockMvc.perform(get("/api/v1/share-groups/non-existent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Share group 'non-existent' not found"));
    }
}
