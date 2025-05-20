package com.tiger.capstone.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TaskUsers {
    @Column(name="task_user_id")
    @JsonProperty("task_user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskUserId;

    @Column(name="task_id")
    @JsonProperty("task_id")
    private String taskId;

    @Column(name="employee_id")
    @JsonProperty("employee_id")
    private String employeeId;

    
    public TaskUsers() {
    }

    public TaskUsers(int taskUserId, String taskId, String employeeId) {
        this.taskUserId = taskUserId;
        this.taskId = taskId;
        this.employeeId = employeeId;
    }

    public int getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(int taskUserId) {
        this.taskUserId = taskUserId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    
    
}
