package com.tiger.capstone.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="userrole")
public class UserRole {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_role_id")
    @JsonProperty("user_role_id")
    private int userRoleId;
    @Column(name="role_id")
    @JsonProperty("role_id")
    private int roleId;
    @Column(name="employee_id")
    @JsonProperty("employee_id")
    private String employeeId;
    @Column(name="project_id")
    @JsonProperty("project_id")
    private String projectId;
    public UserRole(){

    }
    public UserRole(int userRoleId, int roleId, String employeeId, String projectId) {
        this.userRoleId = userRoleId;
        this.roleId = roleId;
        this.employeeId = employeeId;
        this.projectId = projectId;
    }
    public int getUserRoleId() {
        return userRoleId;
    }
    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }
    public int getRoleId() {
        return roleId;
    }
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public String getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
}
