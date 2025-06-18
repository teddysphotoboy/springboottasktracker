package com.tiger.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.capstone.dto.TokenRequestDTO;
import com.tiger.capstone.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAuthenticateUser_ReturnsSuccessMessage() throws Exception {
        String token = "mock-token";
        String userId = "google-123";

        Mockito.when(authenticationService.getGoogleId(token)).thenReturn(userId);

        TokenRequestDTO request = new TokenRequestDTO();
        request.setToken(token);

        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Authentication successful for user: " + userId));
    }

    @Test
    void testAuthenticateUser_InvalidToken_ThrowsException() throws Exception {
        String token = "bad-token";

        Mockito.when(authenticationService.getGoogleId(token))
                .thenThrow(new RuntimeException("Invalid token"));

        TokenRequestDTO request = new TokenRequestDTO();
        request.setToken(token);

        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError()); // Customize if you use exception handlers
    }
}

