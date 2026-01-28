package com.kafkaadmin.user;

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
class UserServiceTest {

    @Mock
    private KafkaAdminPort kafkaAdminPort;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(kafkaAdminPort);
    }

    @Test
    void listScramCredentials_shouldReturnSortedCredentials() {
        // Given
        List<UserScramCredential> credentials = List.of(
                new UserScramCredential("bob", List.of(new ScramCredentialInfo("SCRAM-SHA-256", 4096))),
                new UserScramCredential("alice", List.of(new ScramCredentialInfo("SCRAM-SHA-512", 8192)))
        );

        when(kafkaAdminPort.describeUserScramCredentials()).thenReturn(credentials);

        // When
        List<UserScramCredentialResponse> result = userService.listScramCredentials();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("alice");
        assertThat(result.get(1).name()).isEqualTo("bob");
    }

    @Test
    void listScramCredentials_whenEmpty_shouldReturnEmptyList() {
        // Given
        when(kafkaAdminPort.describeUserScramCredentials()).thenReturn(List.of());

        // When
        List<UserScramCredentialResponse> result = userService.listScramCredentials();

        // Then
        assertThat(result).isEmpty();
    }
}
