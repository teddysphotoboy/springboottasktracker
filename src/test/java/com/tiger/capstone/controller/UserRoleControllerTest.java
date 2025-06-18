package com.tiger.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.capstone.dto.UserRoleDTO;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.UserRoleRepository;
import com.tiger.capstone.service.AdminService;
import com.tiger.capstone.service.AuthenticationService;
import com.tiger.capstone.service.UserRoleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRoleController.class)
class UserRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRoleService userRoleService;

    @MockitoBean
    private UserRoleRepository userRoleRepository;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private AdminService adminService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateUserRole_ReturnsCreated() throws Exception {
        UserRoleDTO dto = new UserRoleDTO();
        dto.setProjectId("proj-1");
        dto.setRoleId(2);

        Mockito.when(userRoleService.createUserRole(eq("user-1"), any(UserRoleDTO.class)))
                .thenReturn("Role created successfully");

        mockMvc.perform(post("/create-user-role/user-1")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.detail").value("Role created successfully"));
    }

    @Test
    void testCreateUserRole_ThrowsDatabaseException() throws Exception {
        UserRoleDTO dto = new UserRoleDTO();
        dto.setProjectId("proj-1");
        dto.setRoleId(2);

        Mockito.when(userRoleService.createUserRole(eq("user-1"), any(UserRoleDTO.class)))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/create-user-role/user-1")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetUserRoleByProject_ReturnsRoleId() throws Exception {
        UserRole userRole = new UserRole();
        userRole.setRoleId(1);

        Mockito.when(userRoleRepository.findByEmployeeIdAndProjectId("user-1", "proj-1"))
                .thenReturn(userRole);

        mockMvc.perform(get("/user-role/user-1/proj-1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void testGetUserRoleByProject_ReturnsZero() throws Exception {
        Mockito.when(userRoleRepository.findByEmployeeIdAndProjectId("user-1", "proj-1"))
                .thenReturn(null);

        mockMvc.perform(get("/user-role/user-1/proj-1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void testDeleteUserRole_Success() throws Exception {
        UserRole role = new UserRole();
        role.setRoleId(3);

        Mockito.when(authenticationService.getGoogleId("valid-token")).thenReturn("admin-id");
        Mockito.when(adminService.checkAdmin("admin-id")).thenReturn(true);
        Mockito.when(userRoleRepository.findByEmployeeIdAndProjectId("user-2", "proj-1")).thenReturn(role);

        mockMvc.perform(delete("/delete-user/3/user-1/proj-1/user-2/")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Deleted"));
    }

    @Test
    void testDeleteUserRole_NotAdmin() throws Exception {
        Mockito.when(authenticationService.getGoogleId("bad-token")).thenReturn("user-id");
        Mockito.when(adminService.checkAdmin("user-id")).thenReturn(false);

        mockMvc.perform(delete("/delete-user/3/user-1/proj-1/user-2/")
                        .header("Authorization", "Bearer bad-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteUserRole_UserRoleNotFound() throws Exception {
        Mockito.when(authenticationService.getGoogleId("valid-token")).thenReturn("admin-id");
        Mockito.when(adminService.checkAdmin("admin-id")).thenReturn(true);
        Mockito.when(userRoleRepository.findByEmployeeIdAndProjectId("user-2", "proj-1")).thenReturn(null);

        mockMvc.perform(delete("/delete-user/3/user-1/proj-1/user-2/")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUserRole_ThrowsDatabaseException() throws Exception {
        UserRole role = new UserRole();
        role.setRoleId(3);

        Mockito.when(authenticationService.getGoogleId("token")).thenReturn("admin-id");
        Mockito.when(adminService.checkAdmin("admin-id")).thenReturn(true);
        Mockito.when(userRoleRepository.findByEmployeeIdAndProjectId("user-2", "proj-1")).thenReturn(role);
        Mockito.doThrow(new RuntimeException("DB error"))
               .when(userRoleRepository).deleteByRoleIdAndEmployeeIdAndProjectId(3, "user-2", "proj-1");

        mockMvc.perform(delete("/delete-user/3/user-1/proj-1/user-2/")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isInternalServerError());
    }
}
