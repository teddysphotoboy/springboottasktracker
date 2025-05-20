package com.tiger.capstone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tiger.capstone.dto.TaskDTO;
import com.tiger.capstone.model.Task;
import com.tiger.capstone.repository.TaskRepository;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repo;

    public Task createTask(TaskDTO dto){
        Task task=new Task();
        task.setTaskName(dto.getTaskName());
        task.setProjectId(dto.getProjectId());
        task.setDueDate(dto.getDueDate());
        task.setTaskDescription(dto.getTaskDescription());
        task.setTaskOwnerId(dto.getTaskOwnerId());
        task.setTaskStatus(dto.getTaskStatus());
        return repo.save(task);
    }
    public Task getTask(String id){
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
    public void updateTask(String taskId, String userId, String projectId, TaskDTO dto) {
        Task task=repo.findById(taskId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found"));
        task.setDueDate(dto.getDueDate());
        task.setTaskDescription(dto.getTaskDescription());
        task.setTaskName(dto.getTaskName());
        task.setTaskOwnerId(dto.getTaskOwnerId());
        task.setTaskStatus(dto.getTaskStatus());
        repo.save(task);
    }
}
