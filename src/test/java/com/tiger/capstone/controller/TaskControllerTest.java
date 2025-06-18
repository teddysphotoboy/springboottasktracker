package com.tiger.capstone.controller;
import com.tiger.capstone.dto.TaskDTO;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.repository.TaskRepository;
import com.tiger.capstone.repository.TaskUsersRepository;
import com.tiger.capstone.service.AuthenticationService;
import com.tiger.capstone.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private TaskUsersRepository taskUsersRepository;

    @MockitoBean
    private AuthenticationService authenticationService;

    private Task sampleTask;

    private TaskDTO sampleDTO;

    @BeforeEach
    void setup() {
        sampleTask = new Task();
        sampleTask.setTaskId("task-1");
        sampleTask.setTaskName("Test Task");
        sampleTask.setProjectId("proj-1");
        sampleTask.setTaskDescription("A sample task");
        sampleTask.setDueDate(LocalDate.of(2025, 6, 15));
        sampleTask.setTaskOwnerId("user-1");
        sampleTask.setTaskStatus("OPEN");

        sampleDTO = new TaskDTO();
        sampleDTO.setTaskName("Test Task");
        sampleDTO.setProjectId("proj-1");
        sampleDTO.setTaskDescription("A sample task");
        sampleDTO.setDueDate(LocalDate.of(2025, 6, 15));
        sampleDTO.setTaskOwnerId("user-1");
        sampleDTO.setTaskStatus("OPEN");
    }

    @Test
    void testGetTask() throws Exception {
        Mockito.when(taskService.getTask("task-1")).thenReturn(sampleTask);

        mockMvc.perform(get("/tasks/task-1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task_name").value("Test Task"));
    }

    @Test
    void testCreateTask() throws Exception {
        Mockito.when(taskService.createTask(any(TaskDTO.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/create-task/proj-1/user-1")
        .header("Authorization", "Bearer token")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
                "taskName": "Test Task",
                "projectId": "proj-1",
                "dueDate": "2025-06-15",
                "taskDescription": "This is a test task",
                "taskOwnerId": "user-1",
                "taskStatus": "Pending"
            }
        """))
    .andExpect(status().isCreated())
    .andExpect(jsonPath("$.task_name").value("Test Task"));

    }

    @Test
void testUpdateTask() throws Exception {
    mockMvc.perform(put("/update-task/task-1/user-1/proj-1")
            .header("Authorization", "Bearer token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "taskName": "Test Task",
                    "projectId": "proj-1",
                    "dueDate": "2025-06-15",
                    "taskDescription": "This is a test task",
                    "taskOwnerId": "user-1",
                    "taskStatus": "Pending"
                }
            """))
        .andExpect(status().isNoContent());

    Mockito.verify(taskService).updateTask(eq("task-1"), eq("user-1"), eq("proj-1"), any(TaskDTO.class));
}


    @Test
    void testGetProjectTasks() throws Exception {
        Mockito.when(taskRepository.findAllByProjectId("proj-1")).thenReturn(Arrays.asList(sampleTask));

        mockMvc.perform(get("/proj-1/tasks")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].task_name").value("Test Task"));
    }

    @Test
    void testDeleteProjectTasks() throws Exception {
        Mockito.doNothing().when(taskUsersRepository).deleteByTaskId("task-1");
        Mockito.doNothing().when(taskRepository).deleteById("task-1");

        mockMvc.perform(delete("/tasks/task-1/user-1/proj-1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));
    }
}

