package com.tiger.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.capstone.dto.ProjectDTO;
import com.tiger.capstone.model.Project;
import com.tiger.capstone.service.AdminService;
import com.tiger.capstone.service.AuthenticationService;
import com.tiger.capstone.service.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private com.tiger.capstone.repository.ProjectRepository projectRepository;

    @MockitoBean
    private com.tiger.capstone.repository.UserRoleRepository userRoleRepository;

    @MockitoBean
    private com.tiger.capstone.repository.TaskUsersRepository taskUsersRepository;

    @MockitoBean
    private com.tiger.capstone.repository.TaskRepository taskRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetProject() throws Exception {
        Project project = new Project();
        project.setProjectId("123");
        project.setProjectName("Demo Project");

        when(projectService.getProject("123")).thenReturn(project);

        mockMvc.perform(get("/projects/123/testUser")
                .header("Authorization", "Bearer dummyToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.project_name").value("Demo Project"));
    }

    @Test
    void testCreateProject_AsAdmin() throws Exception {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectName("Test Project");

        Project project = new Project();
        project.setProjectId("1");
        project.setProjectName("Test Project");

        when(authenticationService.getGoogleId("dummyToken")).thenReturn("adminId");
        when(adminService.checkAdmin("adminId")).thenReturn(true);
        when(projectService.createProject(any(ProjectDTO.class))).thenReturn(project);

        mockMvc.perform(post("/create-project/testUser")
                .header("Authorization", "Bearer dummyToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.project_name").value("Test Project"));
    }

    @Test
    void testCreateProject_NotAdmin() throws Exception {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectName("Test Project");

        when(authenticationService.getGoogleId("dummyToken")).thenReturn("userId");
        when(adminService.checkAdmin("userId")).thenReturn(false);

        mockMvc.perform(post("/create-project/testUser")
                .header("Authorization", "Bearer dummyToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Only admin can create projects."));
    }

    @Test
    void testGetUserProjects() throws Exception {
        Project project1 = new Project();
        project1.setProjectId("1");
        project1.setProjectName("Project A");

        Project project2 = new Project();
        project2.setProjectId("2");
        project2.setProjectName("Project B");

        List<Project> projects = Arrays.asList(project1, project2);

        when(authenticationService.getGoogleId("dummyToken")).thenReturn("user123");
        when(projectService.getUserProjects("user123")).thenReturn(projects);

        mockMvc.perform(get("/projects/testUser")
                .header("Authorization", "Bearer dummyToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].project_name").value("Project A"))
                .andExpect(jsonPath("$[1].project_name").value("Project B"));
    }
}
