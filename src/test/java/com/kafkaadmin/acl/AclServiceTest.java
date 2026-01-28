package com.kafkaadmin.acl;

import com.kafkaadmin.common.KafkaAdminPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AclServiceTest {

    @Mock
    private KafkaAdminPort kafkaAdminPort;

    private AclService aclService;

    @BeforeEach
    void setUp() {
        aclService = new AclService(kafkaAdminPort);
    }

    @Test
    void listAcls_shouldReturnSortedAcls() {
        // Given
        List<Acl> acls = List.of(
                new Acl("TOPIC", "topic-b", "LITERAL", "User:bob", "*", "READ", "ALLOW"),
                new Acl("GROUP", "group-a", "LITERAL", "User:alice", "*", "READ", "ALLOW"),
                new Acl("TOPIC", "topic-a", "LITERAL", "User:alice", "*", "WRITE", "ALLOW")
        );

        when(kafkaAdminPort.describeAcls()).thenReturn(acls);

        // When
        List<AclResponse> result = aclService.listAcls();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).resourceType()).isEqualTo("GROUP");
        assertThat(result.get(1).resourceType()).isEqualTo("TOPIC");
        assertThat(result.get(1).resourceName()).isEqualTo("topic-a");
        assertThat(result.get(2).resourceName()).isEqualTo("topic-b");
    }

    @Test
    void listAcls_whenEmpty_shouldReturnEmptyList() {
        // Given
        when(kafkaAdminPort.describeAcls()).thenReturn(List.of());

        // When
        List<AclResponse> result = aclService.listAcls();

        // Then
        assertThat(result).isEmpty();
    }
}
