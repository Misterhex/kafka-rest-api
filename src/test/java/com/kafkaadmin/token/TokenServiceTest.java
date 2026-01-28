package com.kafkaadmin.token;

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
class TokenServiceTest {

    @Mock
    private KafkaAdminPort kafkaAdminPort;

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(kafkaAdminPort);
    }

    @Test
    void listTokens_shouldReturnSortedTokens() {
        // Given
        List<DelegationToken> tokens = List.of(
                new DelegationToken("token-b", "User:admin", "User:admin", List.of(), 1000L, 2000L, 3000L),
                new DelegationToken("token-a", "User:alice", "User:alice", List.of("User:bob"), 1100L, 2100L, 3100L)
        );

        when(kafkaAdminPort.describeDelegationTokens()).thenReturn(tokens);

        // When
        List<DelegationTokenResponse> result = tokenService.listTokens();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).tokenId()).isEqualTo("token-a");
        assertThat(result.get(1).tokenId()).isEqualTo("token-b");
    }

    @Test
    void listTokens_whenEmpty_shouldReturnEmptyList() {
        // Given
        when(kafkaAdminPort.describeDelegationTokens()).thenReturn(List.of());

        // When
        List<DelegationTokenResponse> result = tokenService.listTokens();

        // Then
        assertThat(result).isEmpty();
    }
}
