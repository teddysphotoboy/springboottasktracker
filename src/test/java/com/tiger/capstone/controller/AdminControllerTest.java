package com.tiger.capstone.controller;
import com.tiger.capstone.service.AdminService;
import com.tiger.capstone.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    void testIsAdmin_ReturnsTrueForAdmin() throws Exception {
        String token = "mock-token";
        String googleId = "google-123";
        String authHeader = "Bearer " + token;

        Mockito.when(authenticationService.getGoogleId(token)).thenReturn(googleId);
        Mockito.when(adminService.checkAdmin(googleId)).thenReturn(true);

        mockMvc.perform(get("/is_admin/emp-1")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testIsAdmin_ReturnsFalseForNonAdmin() throws Exception {
        String token = "mock-token";
        String googleId = "google-456";
        String authHeader = "Bearer " + token;

        Mockito.when(authenticationService.getGoogleId(token)).thenReturn(googleId);
        Mockito.when(adminService.checkAdmin(googleId)).thenReturn(false);

        mockMvc.perform(get("/is_admin/emp-2")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testIsAdmin_InvalidToken_ThrowsUnauthorized() throws Exception {
        String token = "invalid-token";
        String authHeader = "Bearer " + token;

        Mockito.when(authenticationService.getGoogleId(token))
                .thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(get("/is_admin/emp-3")
                        .header("Authorization", authHeader))
                .andExpect(status().isUnauthorized());
    }
}

