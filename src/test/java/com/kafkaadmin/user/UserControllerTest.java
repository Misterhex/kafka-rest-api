package com.kafkaadmin.user;

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
class UserControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UserController controller = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listScramCredentials_shouldReturnCredentials() throws Exception {
        // Given
        List<UserScramCredentialResponse> credentials = List.of(
                new UserScramCredentialResponse("alice", List.of(
                        new ScramCredentialInfoResponse("SCRAM-SHA-256", 4096)
                ))
        );
        when(userService.listScramCredentials()).thenReturn(credentials);

        // When/Then
        mockMvc.perform(get("/api/v1/users/credentials")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("alice"))
                .andExpect(jsonPath("$[0].credentialInfos[0].mechanism").value("SCRAM-SHA-256"));
    }
}
