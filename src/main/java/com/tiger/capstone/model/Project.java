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
@Table(name="projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "project_id")
    @JsonProperty("project_id")
    private String projectId;
    @Column(name = "project_name")
    @JsonProperty("project_name")
    private String projectName;
    @Column(name = "project_description")
    @JsonProperty("project_description")
    private String projectDescription;
    @Column(name = "start_date")
    @JsonProperty("start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    @JsonProperty("end_date")
    private LocalDate endDate;
    @Column(name = "project_owner_id")
    @JsonProperty("project_owner_id")
    private String projectOwnerId;

    public Project(){

    }
    public Project(String projectId, String projectName, String projectDescription, LocalDate startDate,
            LocalDate endDate, String projectOwnerId) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectOwnerId = projectOwnerId;
    }
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectDescription() {
        return projectDescription;
    }
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public String getProjectOwnerId() {
        return projectOwnerId;
    }
    public void setProjectOwnerId(String projectOwnerId) {
        this.projectOwnerId = projectOwnerId;
    }

}
