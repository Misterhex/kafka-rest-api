package com.kafkaadmin.acl;

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
class AclControllerTest {

    @Mock
    private AclService aclService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AclController controller = new AclController(aclService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listAcls_shouldReturnAcls() throws Exception {
        // Given
        List<AclResponse> acls = List.of(
                new AclResponse("TOPIC", "my-topic", "LITERAL", "User:alice", "*", "READ", "ALLOW"),
                new AclResponse("TOPIC", "my-topic", "LITERAL", "User:bob", "*", "WRITE", "ALLOW")
        );
        when(aclService.listAcls()).thenReturn(acls);

        // When/Then
        mockMvc.perform(get("/api/v1/acls")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].resourceType").value("TOPIC"))
                .andExpect(jsonPath("$[0].principal").value("User:alice"))
                .andExpect(jsonPath("$[0].operation").value("READ"));
    }

    @Test
    void listAcls_whenEmpty_shouldReturnEmptyArray() throws Exception {
        // Given
        when(aclService.listAcls()).thenReturn(List.of());

        // When/Then
        mockMvc.perform(get("/api/v1/acls")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
