package com.tiger.capstone.model;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "task_id", nullable = false, updatable = false, unique = true)
    @JsonProperty("task_id")
    private String taskId;
    @Column(name="task_name")
    @JsonProperty("task_name")
    private String taskName;
    @Column(name="task_description")
    @JsonProperty("task_description")
    private String taskDescription;
    @Column(name="task_status")
    @JsonProperty("task_status")
    private String taskStatus;
    @Column(name="task_owner_id")
    @JsonProperty("task_owner_id")
    private String taskOwnerId;
    @Column(name="due_date")
    @JsonProperty("due_date")
    private LocalDate dueDate;
    @Column(name="project_id")
    @JsonProperty("project_id")
    private String projectId;
    public Task(){
    }
    public Task(String taskId, String taskName, String taskDescription, String taskStatus, String taskOwnerId,
            LocalDate dueDate, String projectId) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.taskOwnerId = taskOwnerId;
        this.dueDate = dueDate;
        this.projectId = projectId;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

