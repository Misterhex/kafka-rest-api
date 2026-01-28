package com.kafkaadmin.token;

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
class TokenControllerTest {

    @Mock
    private TokenService tokenService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TokenController controller = new TokenController(tokenService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listTokens_shouldReturnTokens() throws Exception {
        // Given
        List<DelegationTokenResponse> tokens = List.of(
                new DelegationTokenResponse("token-1", "User:admin", "User:admin", List.of(), 1000L, 2000L, 3000L)
        );
        when(tokenService.listTokens()).thenReturn(tokens);

        // When/Then
        mockMvc.perform(get("/api/v1/tokens")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].tokenId").value("token-1"))
                .andExpect(jsonPath("$[0].owner").value("User:admin"));
    }
}
