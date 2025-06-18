package com.tiger.capstone.service;

import com.tiger.capstone.dto.TaskDTO;
import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.exception.ResourceNotFoundException;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.repository.TaskRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockitoBean
    private TaskRepository taskRepository;

    private TaskDTO getSampleTaskDto() {
        TaskDTO dto = new TaskDTO();
        dto.setTaskName("Sample Task");
        dto.setProjectId("proj-1");
        dto.setTaskDescription("Description");
        dto.setTaskOwnerId("owner-1");
        dto.setTaskStatus("Pending");
        return dto;
    }

    @Test
    void testCreateTask_Success() {
        TaskDTO dto = getSampleTaskDto();
        Task mockTask = new Task();
        mockTask.setTaskName(dto.getTaskName());

        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        Task result = taskService.createTask(dto);

        assertEquals("Sample Task", result.getTaskName());
    }

    @Test
    void testCreateTask_Failure() {
        TaskDTO dto = getSampleTaskDto();

        when(taskRepository.save(any(Task.class))).thenThrow(new RuntimeException("DB error"));

        assertThrows(DatabaseException.class, () -> taskService.createTask(dto));
    }

    @Test
    void testGetTask_Success() {
        Task task = new Task();
        task.setTaskName("Test Task");

        when(taskRepository.findById("task-1")).thenReturn(Optional.of(task));

        Task result = taskService.getTask("task-1");

        assertEquals("Test Task", result.getTaskName());
    }

    @Test
    void testGetTask_NotFound() {
        when(taskRepository.findById("task-404")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTask("task-404"));
    }

    @Test
    void testUpdateTask_Success() {
        Task existingTask = new Task();
        existingTask.setTaskId("task-1");

        TaskDTO dto = getSampleTaskDto();
        dto.setTaskName("Updated Name");

        when(taskRepository.findById("task-1")).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        assertDoesNotThrow(() ->
            taskService.updateTask("task-1", "user-1", "proj-1", dto)
        );

        verify(taskRepository).save(existingTask);
    }

    @Test
    void testUpdateTask_NotFound() {
        TaskDTO dto = getSampleTaskDto();

        when(taskRepository.findById("task-404")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            taskService.updateTask("task-404", "user-1", "proj-1", dto)
        );
    }

    @Test
    void testUpdateTask_SaveFails() {
        Task task = new Task();
        TaskDTO dto = getSampleTaskDto();

        when(taskRepository.findById("task-1")).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenThrow(new RuntimeException("DB down"));

        assertThrows(DatabaseException.class, () ->
            taskService.updateTask("task-1", "user-1", "proj-1", dto)
        );
    }
}
