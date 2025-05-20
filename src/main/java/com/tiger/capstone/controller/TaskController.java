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
import org.springframework.web.bind.annotation.RestController;

import com.tiger.capstone.dto.TaskDTO;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.repository.TaskRepository;
import com.tiger.capstone.repository.TaskUsersRepository;
import com.tiger.capstone.service.TaskService;

import jakarta.transaction.Transactional;

@RestController
public class TaskController {
    @Autowired
    private TaskService service;

    @Autowired
    private TaskRepository repo;

    @Autowired
    private TaskUsersRepository taskUsersRepository;


    @GetMapping("/tasks/{taskId}")
    public Task getTask(@PathVariable String taskId){
        return service.getTask(taskId);
    }
    @PostMapping("/create-task/{projectId}/{userId}")
    public ResponseEntity<Task> createTask(@PathVariable String projectId,@PathVariable String userId, @RequestBody TaskDTO dto){
        Task task=service.createTask(dto);
        return new ResponseEntity<>(task,HttpStatus.CREATED);
    }
    @PutMapping("/update-task/{taskId}/{userId}/{projectId}")
    public void updateTask(@PathVariable String taskId,@PathVariable String userId,@PathVariable String projectId, @RequestBody TaskDTO dto){
        service.updateTask(taskId,userId,projectId,dto);

    }

    @GetMapping("/{projectId}/tasks")
    public List<Task> getProjectTasks(@PathVariable String projectId){
        List<Task> tasks=repo.findAllByProjectId(projectId);
        return tasks;
    }

    @DeleteMapping("/tasks/{taskId}/{userId}/{projectId}")
    @Transactional
    public ResponseEntity<String> deleteProjectTasks(@PathVariable String taskId,@PathVariable String userId,@PathVariable String projectId){
        taskUsersRepository.deleteByTaskId(taskId);
        repo.deleteById(taskId);
        return ResponseEntity.ok("Successfully deleted");
    }
}
