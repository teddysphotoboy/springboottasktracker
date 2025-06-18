package com.tiger.capstone.service;

import com.tiger.capstone.dto.ProjectDTO;
import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.exception.ResourceNotFoundException;
import com.tiger.capstone.model.Project;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.ProjectRepository;
import com.tiger.capstone.repository.UserRoleRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private UserRoleRepository userRoleRepository;

    @MockitoBean
    private AdminService adminService;

    private ProjectDTO getSampleDto() {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectName("Test Project");
        dto.setProjectDescription("Test Description");
        dto.setProjectOwnerId("owner1");
        return dto;
    }

    @Test
    void testCreateProject_Success() {
        ProjectDTO dto = getSampleDto();
        Project project = new Project();
        project.setProjectName(dto.getProjectName());

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.createProject(dto);

        assertEquals("Test Project", result.getProjectName());
    }

    @Test
    void testCreateProject_ThrowsDatabaseException() {
        ProjectDTO dto = getSampleDto();

        when(projectRepository.save(any(Project.class)))
                .thenThrow(new RuntimeException("DB Error"));

        assertThrows(DatabaseException.class, () -> projectService.createProject(dto));
    }

    @Test
    void testGetProject_Success() {
        Project project = new Project();
        project.setProjectName("Sample");

        when(projectRepository.findById("proj-1"))
                .thenReturn(Optional.of(project));

        Project result = projectService.getProject("proj-1");

        assertEquals("Sample", result.getProjectName());
    }

    @Test
    void testGetProject_NotFound() {
        when(projectRepository.findById("proj-404")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.getProject("proj-404"));
    }

    @Test
    void testUpdateProject_Success() {
        Project existing = new Project();
        existing.setProjectId("proj-1");

        ProjectDTO dto = getSampleDto();
        dto.setProjectName("Updated Name");

        when(projectRepository.findById("proj-1")).thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class))).thenReturn(existing);

        assertDoesNotThrow(() -> projectService.updateProject("user-1", "proj-1", dto));
        verify(projectRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateProject_NotFound() {
        ProjectDTO dto = getSampleDto();

        when(projectRepository.findById("proj-404")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.updateProject("user-1", "proj-404", dto);
        });
    }

    @Test
    void testUpdateProject_ThrowsDatabaseException() {
        Project existing = new Project();
        ProjectDTO dto = getSampleDto();

        when(projectRepository.findById("proj-1")).thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class))).thenThrow(new RuntimeException("DB Error"));

        assertThrows(DatabaseException.class, () -> {
            projectService.updateProject("user-1", "proj-1", dto);
        });
    }

    @Test
    void testGetUserProjects_AdminUser() {
        List<Project> allProjects = List.of(new Project(), new Project());
        when(adminService.checkAdmin("admin1")).thenReturn(true);
        when(projectRepository.findAll()).thenReturn(allProjects);

        List<Project> result = projectService.getUserProjects("admin1");

        assertEquals(2, result.size());
    }

    @Test
    void testGetUserProjects_NonAdmin() {
        UserRole role1 = new UserRole();
        role1.setProjectId("p1");
        UserRole role2 = new UserRole();
        role2.setProjectId("p2");

        Project project1 = new Project();
        project1.setProjectId("p1");

        when(adminService.checkAdmin("user1")).thenReturn(false);
        when(userRoleRepository.findByEmployeeId("user1")).thenReturn(List.of(role1, role2));
        when(projectRepository.findByProjectId("p1")).thenReturn(project1);
        when(projectRepository.findByProjectId("p2")).thenReturn(null); // simulate one missing

        List<Project> result = projectService.getUserProjects("user1");

        assertEquals(1, result.size());
        assertEquals("p1", result.get(0).getProjectId());
    }

    @Test
    void testGetUserProjects_ThrowsDatabaseException() {
        when(adminService.checkAdmin("user1")).thenThrow(new RuntimeException("DB failed"));

        assertThrows(DatabaseException.class, () -> projectService.getUserProjects("user1"));
    }
}
