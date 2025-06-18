package com.tiger.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.model.TaskUsers;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.TaskRepository;
import com.tiger.capstone.repository.TaskUsersRepository;
import com.tiger.capstone.repository.UserRoleRepository;
import com.tiger.capstone.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskUsersController.class)
class TaskUsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskUsersRepository repo;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserRoleRepository userRoleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testReadOnlyUser_ReturnsTasks() throws Exception {
        TaskUsers tu = new TaskUsers();
        tu.setTaskId("task-1");
        Task task = new Task();
        task.setTaskId("task-1");
        task.setProjectId("proj-1");

        Mockito.when(repo.findByEmployeeId("user-1")).thenReturn(Collections.singletonList(tu));
        Mockito.when(taskRepository.findByProjectIdAndTaskId("proj-1", "task-1")).thenReturn(task);

        mockMvc.perform(get("/task/read-only-user/proj-1/user-1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].task_id").value("task-1"));
    }

    @Test
    void testReadOnlyUser_ThrowsDatabaseException() throws Exception {
        Mockito.when(repo.findByEmployeeId(anyString())).thenThrow(new RuntimeException("DB down"));

        mockMvc.perform(get("/task/read-only-user/proj-1/user-1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isInternalServerError()); 
    }

    @Test
    void testCreateReadOnlyUser_Success() throws Exception {
        TaskUsers tu = new TaskUsers();
        tu.setEmployeeId("user-1");
        tu.setTaskId("task-1");

        Mockito.when(repo.findByEmployeeIdAndTaskId("user-1", "task-1")).thenReturn(null);
        Mockito.when(repo.save(any(TaskUsers.class))).thenReturn(tu);

        mockMvc.perform(post("/create-read-only-user/user-1/proj-1/task-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tu))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Read-only user created"));
    }

    @Test
    void testCreateReadOnlyUser_ThrowsDatabaseException() throws Exception {
        TaskUsers tu = new TaskUsers();
        tu.setEmployeeId("user-1");
        tu.setTaskId("task-1");

        Mockito.when(repo.findByEmployeeIdAndTaskId(anyString(), anyString()))
                .thenThrow(new RuntimeException("DB issue"));

        mockMvc.perform(post("/create-read-only-user/user-1/proj-1/task-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tu))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteReadOnlyUser_Success() throws Exception {
        TaskUsers tu = new TaskUsers();
        tu.setEmployeeId("user-1");
        tu.setTaskId("task-1");

        UserRole role = new UserRole();

        Mockito.when(repo.findByEmployeeIdAndTaskId("user-1", "task-1")).thenReturn(tu);
        Mockito.when(userRoleRepository.findByEmployeeIdAndProjectId("user-1", "proj-1")).thenReturn(role);

        mockMvc.perform(delete("/delete-read-only-user/creator-1/proj-1/user-1/task-1/")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Read-only user deleted"));
    }

    @Test
    void testDeleteReadOnlyUser_NotFound() throws Exception {
        Mockito.when(repo.findByEmployeeIdAndTaskId("user-1", "task-1")).thenReturn(null);

        mockMvc.perform(delete("/delete-read-only-user/creator-1/proj-1/user-1/task-1/")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound()); 
    }

    @Test
    void testDeleteReadOnlyUser_ThrowsDatabaseException() throws Exception {
        TaskUsers tu = new TaskUsers();
        tu.setEmployeeId("user-1");
        tu.setTaskId("task-1");

        Mockito.when(repo.findByEmployeeIdAndTaskId(anyString(), anyString())).thenReturn(tu);
        Mockito.doThrow(new RuntimeException("DB fail")).when(repo).delete(any(TaskUsers.class));

        mockMvc.perform(delete("/delete-read-only-user/creator-1/proj-1/user-1/task-1/")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isInternalServerError());
    }
}
