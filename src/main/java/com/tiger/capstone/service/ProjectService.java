package com.tiger.capstone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tiger.capstone.dto.ProjectDTO;
import com.tiger.capstone.model.Project;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.ProjectRepository;
import com.tiger.capstone.repository.UserRoleRepository;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository repo;

    @Autowired
    private UserRoleRepository userRoleRepo;

    @Autowired
    private AdminService adminService;

    public Project createProject(ProjectDTO dto){
        Project project=new Project();
        project.setProjectName(dto.getProjectName());
        project.setProjectDescription(dto.getProjectDescription());
        project.setEndDate(dto.getEndDate());
        project.setProjectOwnerId(dto.getProjectOwnerId());
        project.setStartDate(dto.getStartDate());
        return repo.save(project);
    }
    public Project getProject(String id){
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    public void updateProject(String userId, String projectId,ProjectDTO dto){
        Project project=repo.findById(projectId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Project not found"));
        project.setProjectName(dto.getProjectName());
        project.setEndDate(dto.getEndDate());
        project.setProjectDescription(dto.getProjectDescription());
        project.setStartDate(dto.getStartDate());
        repo.save(project);

    }

    public List<Project> getUserProjects(String userId){
        List<Project> projects=new ArrayList<>();
        if(adminService.checkAdmin(userId)){
            return repo.findAll();
        }
        else{
            List<UserRole> roles=userRoleRepo.findByEmployeeId(userId);
            for(UserRole role:roles){
                projects.add(repo.findByProjectId(role.getProjectId()));
            }
                }
        return projects;
    }
}
