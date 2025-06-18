package com.tiger.capstone.controller;

import java.util.List;

import com.tiger.capstone.dto.TaskDTO;
import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.repository.TaskRepository;
import com.tiger.capstone.repository.TaskUsersRepository;
import com.tiger.capstone.service.AuthenticationService;
import com.tiger.capstone.service.TaskService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {
    @Autowired
    private TaskService service;

    @Autowired
    private TaskRepository repo;

    @Autowired 
    private AuthenticationService authenticationService;

    @Autowired
    private TaskUsersRepository taskUsersRepository;

    @GetMapping("/tasks/{taskId}")
    public Task getTask(@PathVariable String taskId, @RequestHeader("Authorization") String authHeader){
        return service.getTask(taskId);
    }

    @PostMapping("/create-task/{projectId}/{userId}")
    public ResponseEntity<Task> createTask(@PathVariable String projectId,
                                           @PathVariable String userId,
                                           @RequestBody TaskDTO dto,
                                           @RequestHeader("Authorization") String authHeader) {
        // Authorization checks can be added here if needed
        Task task = service.createTask(dto);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/update-task/{taskId}/{userId}/{projectId}")
    public ResponseEntity<Void> updateTask(@PathVariable String taskId,
                                           @PathVariable String userId,
                                           @PathVariable String projectId,
                                           @RequestBody TaskDTO dto,
                                           @RequestHeader("Authorization") String authHeader) {
        service.updateTask(taskId, userId, projectId, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/tasks")
    public List<Task> getProjectTasks(@PathVariable String projectId,
                                      @RequestHeader("Authorization") String authHeader){
        try {
            return repo.findAllByProjectId(projectId);
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch tasks for project: " + projectId);
        }
    }

    @DeleteMapping("/tasks/{taskId}/{userId}/{projectId}")
    @Transactional
    public ResponseEntity<String> deleteProjectTasks(@PathVariable String taskId,
                                                     @PathVariable String userId,
                                                     @PathVariable String projectId,
                                                     @RequestHeader("Authorization") String authHeader){
        try {
            taskUsersRepository.deleteByTaskId(taskId);
            repo.deleteById(taskId);
            return ResponseEntity.ok("Successfully deleted");
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete task: " + e.getMessage());
        }
    }
}
