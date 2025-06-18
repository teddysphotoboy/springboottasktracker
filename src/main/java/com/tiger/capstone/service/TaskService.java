package com.tiger.capstone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tiger.capstone.dto.TaskDTO;
import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.exception.ResourceNotFoundException;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.repository.TaskRepository;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repo;

    public Task createTask(TaskDTO dto){
        try {
            Task task = new Task();
            task.setTaskName(dto.getTaskName());
            task.setProjectId(dto.getProjectId());
            task.setDueDate(dto.getDueDate());
            task.setTaskDescription(dto.getTaskDescription());
            task.setTaskOwnerId(dto.getTaskOwnerId());
            task.setTaskStatus(dto.getTaskStatus());
            return repo.save(task);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create task: " + e.getMessage());
        }
    }

    public Task getTask(String id){
        return repo.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Task not found with id: " + id));
    }

    public void updateTask(String taskId, String userId, String projectId, TaskDTO dto) {
        Task task = repo.findById(taskId).orElseThrow(() ->
            new ResourceNotFoundException("Task not found with id: " + taskId));
        try {
            task.setDueDate(dto.getDueDate());
            task.setTaskDescription(dto.getTaskDescription());
            task.setTaskName(dto.getTaskName());
            task.setTaskOwnerId(dto.getTaskOwnerId());
            task.setTaskStatus(dto.getTaskStatus());
            repo.save(task);
        } catch (Exception e) {
            throw new DatabaseException("Failed to update task: " + e.getMessage());
        }
    }
}
