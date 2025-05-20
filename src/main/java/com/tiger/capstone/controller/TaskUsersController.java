package com.tiger.capstone.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tiger.capstone.model.Task;
import com.tiger.capstone.model.TaskUsers;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.TaskRepository;
import com.tiger.capstone.repository.TaskUsersRepository;
import com.tiger.capstone.repository.UserRoleRepository;

@RestController
public class TaskUsersController {
    @Autowired
    private TaskUsersRepository repo;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping("/task/read-only-user/{projectId}/{userId}")
    public List<Task> readOnlyUser(@PathVariable String projectId,@PathVariable String userId){
        List<TaskUsers> taskUsers=repo.findByEmployeeId(userId);
        List<Task> filteredTasks=new ArrayList<>();
        for(TaskUsers user:taskUsers){
            Task task=taskRepository.findByProjectIdAndTaskId(projectId,user.getTaskId());
            filteredTasks.add(task);
        }
        return filteredTasks;
    }
    @PostMapping("/create-read-only-user/{userId}/{projectId}/{taskId}")
    public ResponseEntity<String> createReadOnlyUser(@PathVariable String userId, @PathVariable String projectId,@PathVariable String taskId,@RequestBody TaskUsers taskUser){
        if(repo.findByEmployeeIdAndTaskId(taskUser.getEmployeeId(),taskUser.getTaskId())==null){
            repo.save(taskUser);
        }
        return ResponseEntity.ok("Read only user created");
    }
    @DeleteMapping("/delete-read-only-user/{creatorUserId}/{projectId}/{userId}/{taskId}/")
    public void deleteReadOnlyUser(@PathVariable String creatorUserId,@PathVariable String projectId,@PathVariable String userId,@PathVariable String taskId){
        TaskUsers taskUser=repo.findByEmployeeIdAndTaskId(userId, taskId);
        UserRole userRole=userRoleRepository.findByEmployeeIdAndProjectId(userId, projectId);
        userRoleRepository.delete(userRole);
        repo.delete(taskUser);
    }
}
