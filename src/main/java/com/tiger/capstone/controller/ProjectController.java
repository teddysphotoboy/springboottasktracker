package com.tiger.capstone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tiger.capstone.dto.ProjectDTO;
import com.tiger.capstone.model.Project;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.ProjectRepository;
import com.tiger.capstone.repository.TaskRepository;
import com.tiger.capstone.repository.UserRoleRepository;
import com.tiger.capstone.service.AdminService;
import com.tiger.capstone.service.AuthenticationService;
import com.tiger.capstone.service.ProjectService;

import jakarta.transaction.Transactional;

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

    @GetMapping("/projects/{projectId}/{userId}")
    public Project getProject(@PathVariable String projectId,@PathVariable String userId,@RequestHeader("Authorization") String authHeader){
        return service.getProject(projectId);
    }
    @PostMapping("/create-project/{userId}")
    public ResponseEntity<Project> createProject(@PathVariable String userId,@RequestBody ProjectDTO dto,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String googleId=authenticationService.getGoogleId(token);
        if(adminService.checkAdmin(googleId)){

            Project project=service.createProject(dto);
            return new ResponseEntity<>(project,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/update-project/{projectId}/{userId}")
    public void updateProject(@PathVariable String projectId, @PathVariable String userId, @RequestBody ProjectDTO dto,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String googleId=authenticationService.getGoogleId(token);
        if(adminService.checkAdmin(googleId)){

            service.updateProject(userId, projectId, dto);
        }
    }

    @GetMapping("/projects/{userId}")
    public List<Project> getUserProjects(@PathVariable String userId,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String googleId=authService.getGoogleId(token);
        return service.getUserProjects(googleId);

    }
    @DeleteMapping("delete-project/{projectId}/{userId}")
    @Transactional
    public ResponseEntity<String> deleteProject(@PathVariable String projectId,@PathVariable String userId,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String googleId=authService.getGoogleId(token);
        if(adminService.checkAdmin(googleId)){

            List<UserRole> userRoles=userRoleRepository.findByProjectId(projectId);
            for(UserRole userRole:userRoles){
                userRoleRepository.delete(userRole);
            }
            List<Task> tasks=taskRepository.findAllByProjectId(projectId);
            for(Task task:tasks){
                taskRepository.delete(task);
            }
            repo.deleteById(projectId);
            return ResponseEntity.ok("Project Deleted");
        }
        return ResponseEntity.ok("Forbidden");
    }
}
