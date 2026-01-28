package com.kafkaadmin.sharegroup;

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
class ShareGroupServiceTest {

    @Mock
    private KafkaAdminPort kafkaAdminPort;

    private ShareGroupService shareGroupService;

    @BeforeEach
    void setUp() {
        shareGroupService = new ShareGroupService(kafkaAdminPort);
    }

    @Test
    void listShareGroups_shouldReturnSortedShareGroups() {
        // Given
        ShareGroup group1 = createShareGroup("zebra-group");
        ShareGroup group2 = createShareGroup("alpha-group");

        when(kafkaAdminPort.listShareGroupIds()).thenReturn(List.of("zebra-group", "alpha-group"));
        when(kafkaAdminPort.describeShareGroup("zebra-group")).thenReturn(group1);
        when(kafkaAdminPort.describeShareGroup("alpha-group")).thenReturn(group2);

        // When
        List<ShareGroupResponse> result = shareGroupService.listShareGroups();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).groupId()).isEqualTo("alpha-group");
        assertThat(result.get(1).groupId()).isEqualTo("zebra-group");
    }

    @Test
    void listShareGroups_whenEmpty_shouldReturnEmptyList() {
        // Given
        when(kafkaAdminPort.listShareGroupIds()).thenReturn(List.of());

        // When
        List<ShareGroupResponse> result = shareGroupService.listShareGroups();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getShareGroup_shouldReturnShareGroupDetail() {
        // Given
        String groupId = "test-share-group";
        ShareGroup group = new ShareGroup(
                groupId,
                "Stable",
                1,
                List.of(
                        new ShareGroupMember("consumer-1-id", "consumer-1", "/192.168.1.100",
                                List.of("topic-0", "topic-1")),
                        new ShareGroupMember("consumer-2-id", "consumer-2", "/192.168.1.101",
                                List.of("topic-2"))
                ));

        when(kafkaAdminPort.describeShareGroup(groupId)).thenReturn(group);

        // When
        ShareGroupDetailResponse result = shareGroupService.getShareGroup(groupId);

        // Then
        assertThat(result.groupId()).isEqualTo(groupId);
        assertThat(result.state()).isEqualTo("Stable");
        assertThat(result.coordinatorId()).isEqualTo(1);
        assertThat(result.members()).hasSize(2);
    }

    private ShareGroup createShareGroup(String groupId) {
        return new ShareGroup(
                groupId,
                "Stable",
                1,
                List.of());
    }
}
