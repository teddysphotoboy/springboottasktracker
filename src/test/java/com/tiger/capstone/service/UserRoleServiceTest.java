package com.tiger.capstone.service;

import com.tiger.capstone.dto.UserRoleDTO;
import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.UserRoleRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserRoleServiceTest {

    @Autowired
    private UserRoleService userRoleService;

    @MockitoBean
    private UserRoleRepository userRoleRepository;

    private UserRoleDTO getSampleDTO() {
        UserRoleDTO dto = new UserRoleDTO();
        dto.setEmployeeId("emp-1");
        dto.setProjectId("proj-1");
        dto.setRoleId(2);
        return dto;
    }

    @Test
    void testCreateUserRole_Success() {
        UserRoleDTO dto = getSampleDTO();

        when(userRoleRepository.findByEmployeeIdAndProjectId("emp-1", "proj-1")).thenReturn(null);
        when(userRoleRepository.save(any(UserRole.class))).thenAnswer(i -> i.getArgument(0));

        String result = userRoleService.createUserRole("emp-1", dto);

        assertEquals("Success", result);
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    void testCreateUserRole_UserAlreadyExists() {
        UserRoleDTO dto = getSampleDTO();
        UserRole existing = new UserRole();
        existing.setEmployeeId("emp-1");

        when(userRoleRepository.findByEmployeeIdAndProjectId("emp-1", "proj-1")).thenReturn(existing);

        String result = userRoleService.createUserRole("emp-1", dto);

        assertEquals("User already exists", result);
        verify(userRoleRepository, never()).save(any(UserRole.class));
    }

    @Test
    void testCreateUserRole_DatabaseException() {
        UserRoleDTO dto = getSampleDTO();

        when(userRoleRepository.findByEmployeeIdAndProjectId("emp-1", "proj-1")).thenThrow(new RuntimeException("DB down"));

        assertThrows(DatabaseException.class, () -> userRoleService.createUserRole("emp-1", dto));
    }
}
