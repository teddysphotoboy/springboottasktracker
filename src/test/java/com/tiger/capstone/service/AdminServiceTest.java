package com.tiger.capstone.service;

import com.tiger.capstone.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @MockitoBean
    private AdminRepository adminRepository;

    @Test
    void testCheckAdmin_WhenUserExists_ReturnsTrue() {
        String userId = "admin123";

        when(adminRepository.existsByEmployeeId(userId)).thenReturn(true);

        boolean result = adminService.checkAdmin(userId);

        assertTrue(result);
        verify(adminRepository).existsByEmployeeId(userId);
    }

    @Test
    void testCheckAdmin_WhenUserDoesNotExist_ReturnsFalse() {
        String userId = "user456";

        when(adminRepository.existsByEmployeeId(userId)).thenReturn(false);

        boolean result = adminService.checkAdmin(userId);

        assertFalse(result);
        verify(adminRepository).existsByEmployeeId(userId);
    }
}
