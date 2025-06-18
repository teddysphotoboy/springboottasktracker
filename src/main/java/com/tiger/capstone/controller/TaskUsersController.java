package com.tiger.capstone.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.exception.ResourceNotFoundException;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.model.TaskUsers;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.TaskRepository;
import com.tiger.capstone.repository.TaskUsersRepository;
import com.tiger.capstone.repository.UserRoleRepository;
import com.tiger.capstone.service.AuthenticationService;

@RestController
public class TaskUsersController {
    @Autowired
    private TaskUsersRepository repo;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired 
    private AuthenticationService authenticationService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping("/task/read-only-user/{projectId}/{userId}")
    public List<Task> readOnlyUser(@PathVariable String projectId,
                                   @PathVariable String userId,
                                   @RequestHeader("Authorization") String authHeader) {
        try {
            List<TaskUsers> taskUsers = repo.findByEmployeeId(userId);
            List<Task> filteredTasks = new ArrayList<>();
            for (TaskUsers user : taskUsers) {
                Task task = taskRepository.findByProjectIdAndTaskId(projectId, user.getTaskId());
                if (task != null) {
                    filteredTasks.add(task);
                }
            }
            return filteredTasks;
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch read-only tasks for user: " + userId);
        }
    }

    @PostMapping("/create-read-only-user/{userId}/{projectId}/{taskId}")
    public ResponseEntity<String> createReadOnlyUser(@PathVariable String userId,
                                                     @PathVariable String projectId,
                                                     @PathVariable String taskId,
                                                     @RequestBody TaskUsers taskUser,
                                                     @RequestHeader("Authorization") String authHeader) {
        try {
            TaskUsers existing = repo.findByEmployeeIdAndTaskId(taskUser.getEmployeeId(), taskUser.getTaskId());
            if (existing == null) {
                repo.save(taskUser);
            }
            return ResponseEntity.ok("Read-only user created");
        } catch (Exception e) {
            throw new DatabaseException("Failed to create read-only user");
        }
    }

    @DeleteMapping("/delete-read-only-user/{creatorUserId}/{projectId}/{userId}/{taskId}/")
    public ResponseEntity<String> deleteReadOnlyUser(@PathVariable String creatorUserId,
                                                     @PathVariable String projectId,
                                                     @PathVariable String userId,
                                                     @PathVariable String taskId,
                                                     @RequestHeader("Authorization") String authHeader) {
        try {
            TaskUsers taskUser = repo.findByEmployeeIdAndTaskId(userId, taskId);
            if (taskUser == null) {
                throw new ResourceNotFoundException("Read-only task user not found for deletion");
            }

            UserRole userRole = userRoleRepository.findByEmployeeIdAndProjectId(userId, projectId);
            if (userRole != null) {
                userRoleRepository.delete(userRole);
            }

            repo.delete(taskUser);
            return ResponseEntity.ok("Read-only user deleted");
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete read-only user");
        }
    }
}
