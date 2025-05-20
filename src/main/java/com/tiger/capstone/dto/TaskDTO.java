package com.tiger.capstone.dto;

import java.time.LocalDate;

public class TaskDTO {
    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private String taskOwnerId;
    private LocalDate dueDate;
    private String projectId;
    public TaskDTO(){

    }
    public TaskDTO(String taskName, String taskDescription, String taskStatus, String taskOwnerId, LocalDate dueDate,
            String projectId) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.taskOwnerId = taskOwnerId;
        this.dueDate = dueDate;
        this.projectId = projectId;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getTaskDescription() {
        return taskDescription;
    }
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    public String getTaskStatus() {
        return taskStatus;
    }
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    public String getTaskOwnerId() {
        return taskOwnerId;
    }
    public void setTaskOwnerId(String taskOwnerId) {
        this.taskOwnerId = taskOwnerId;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
}
