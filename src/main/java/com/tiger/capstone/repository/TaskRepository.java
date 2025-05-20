package com.tiger.capstone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiger.capstone.model.Task;

public interface TaskRepository extends JpaRepository<Task,String>{

    public List<Task> findAllByProjectId(String projectId);
    public void deleteAllByProjectId(String projectId);
    Task findByProjectIdAndTaskId(String projectId,String taskId);
} 