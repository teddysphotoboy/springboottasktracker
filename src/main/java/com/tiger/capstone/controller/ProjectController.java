package com.tiger.capstone.controller;

import java.util.List;

import com.tiger.capstone.dto.ProjectDTO;
import com.tiger.capstone.exception.UnauthorizedException;
import com.tiger.capstone.model.Project;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.model.TaskUsers;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.ProjectRepository;
import com.tiger.capstone.repository.TaskRepository;
import com.tiger.capstone.repository.TaskUsersRepository;
import com.tiger.capstone.repository.UserRoleRepository;
import com.tiger.capstone.service.AdminService;
import com.tiger.capstone.service.AuthenticationService;
import com.tiger.capstone.service.ProjectService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {

    @Autowired
    private ProjectService service;

    @Autowired
    private ProjectRepository repo;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private TaskUsersRepository taskUsersRepository;

    @GetMapping("/projects/{projectId}/{userId}")
    public Project getProject(@PathVariable String projectId,
                              @PathVariable String userId,
                              @RequestHeader("Authorization") String authHeader) {
        return service.getProject(projectId);
    }

    @PostMapping("/create-project/{userId}")
    public ResponseEntity<Project> createProject(@PathVariable String userId,
                                                 @RequestBody ProjectDTO dto,
                                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String googleId = authenticationService.getGoogleId(token);

        if (!adminService.checkAdmin(googleId)) {
            throw new UnauthorizedException("Only admin can create projects.");
        }

        Project project = service.createProject(dto);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @PutMapping("/update-project/{projectId}/{userId}")
    public ResponseEntity<Void> updateProject(@PathVariable String projectId,
                                              @PathVariable String userId,
                                              @RequestBody ProjectDTO dto,
                                              @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String googleId = authenticationService.getGoogleId(token);

        if (!adminService.checkAdmin(googleId)) {
            throw new UnauthorizedException("Only admin can update projects.");
        }

        service.updateProject(userId, projectId, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projects/{userId}")
    public List<Project> getUserProjects(@PathVariable String userId,
                                         @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String googleId = authService.getGoogleId(token);
        return service.getUserProjects(googleId);
    }

    @DeleteMapping("/delete-project/{projectId}/{userId}")
    @Transactional
    public ResponseEntity<String> deleteProject(@PathVariable String projectId,
                                                @PathVariable String userId,
                                                @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String googleId = authService.getGoogleId(token);

        if (!adminService.checkAdmin(googleId)) {
            throw new UnauthorizedException("Only admin can delete projects.");
        }

        try {
            List<UserRole> userRoles = userRoleRepository.findByProjectId(projectId);
            userRoleRepository.deleteAll(userRoles);

            

            List<Task> tasks = taskRepository.findAllByProjectId(projectId);
            for(Task task:tasks){
                List<TaskUsers> taskUsers=taskUsersRepository.findByTaskId(task.getTaskId());
                for(TaskUsers taskUser:taskUsers){
                    taskUsersRepository.delete(taskUser);
                }
            }
            taskRepository.deleteAll(tasks);

            repo.deleteById(projectId);
            return ResponseEntity.ok("Project deleted");
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete project: " + e.getMessage());
        }
    }
}
