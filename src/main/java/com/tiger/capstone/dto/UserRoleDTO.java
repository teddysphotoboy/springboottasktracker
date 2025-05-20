package com.tiger.capstone.dto;

public class UserRoleDTO {
    private int roleId;
    private String employeeId;
    private String projectId;
    public UserRoleDTO(){
        
    }
    public UserRoleDTO(int userRoleId, int roleId, String employeeId, String projectId) {
        this.roleId = roleId;
        this.employeeId = employeeId;
        this.projectId = projectId;
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
