package com.tiger.capstone.service;

import java.util.ArrayList;
import java.util.List;

import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.exception.ResourceNotFoundException;
import com.tiger.capstone.model.Project;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.ProjectRepository;
import com.tiger.capstone.repository.UserRoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tiger.capstone.dto.ProjectDTO;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository repo;

    @Autowired
    private UserRoleRepository userRoleRepo;

    @Autowired
    private AdminService adminService;

    public Project createProject(ProjectDTO dto) {
        try {
            Project project = new Project();
            project.setProjectName(dto.getProjectName());
            project.setProjectDescription(dto.getProjectDescription());
            project.setEndDate(dto.getEndDate());
            project.setProjectOwnerId(dto.getProjectOwnerId());
            project.setStartDate(dto.getStartDate());

            return repo.save(project);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create project: " + e.getMessage());
        }
    }

    public Project getProject(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with ID " + id + " not found"));
    }

    public void updateProject(String userId, String projectId, ProjectDTO dto) {
        try {
            Project project = repo.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project with ID " + projectId + " not found"));

            project.setProjectName(dto.getProjectName());
            project.setEndDate(dto.getEndDate());
            project.setProjectDescription(dto.getProjectDescription());
            project.setStartDate(dto.getStartDate());

            repo.save(project);
        } catch (ResourceNotFoundException ex) {
            throw ex; 
        } catch (Exception e) {
            throw new DatabaseException("Failed to update project: " + e.getMessage());
        }
    }

    public List<Project> getUserProjects(String userId) {
        try {
            List<Project> projects = new ArrayList<>();
            if (adminService.checkAdmin(userId)) {
                return repo.findAll();
            } else {
                List<UserRole> roles = userRoleRepo.findByEmployeeId(userId);
                for (UserRole role : roles) {
                    Project project = repo.findByProjectId(role.getProjectId());
                    if (project != null) {
                        projects.add(project);
                    }
                }
            }
            return projects;
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch user projects: " + e.getMessage());
        }
    }
}
