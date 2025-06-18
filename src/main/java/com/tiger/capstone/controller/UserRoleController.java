package com.tiger.capstone.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tiger.capstone.dto.UserRoleDTO;
import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.exception.ResourceNotFoundException;
import com.tiger.capstone.exception.UnauthorizedException;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.UserRoleRepository;
import com.tiger.capstone.service.AdminService;
import com.tiger.capstone.service.AuthenticationService;
import com.tiger.capstone.service.UserRoleService;

@RestController
public class UserRoleController {
    @Autowired
    private UserRoleService service;

    @Autowired 
    private UserRoleRepository repo;

    @Autowired 
    private AuthenticationService authenticationService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-user-role/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createUserRole(@PathVariable String userId, 
                                              @RequestBody UserRoleDTO dto,
                                              @RequestHeader("Authorization") String authHeader) {
        try {
            String message = service.createUserRole(userId, dto);
            return Map.of("detail", message);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create user role.");
        }
    }

    @GetMapping("/user-role/{userId}/{projectId}")
    public ResponseEntity<Integer> getUserRoleByProject(@PathVariable String userId,
                                                        @PathVariable String projectId,
                                                        @RequestHeader("Authorization") String authHeader) {
        try {
            UserRole userRole = repo.findByEmployeeIdAndProjectId(userId, projectId);
            return ResponseEntity.ok(userRole == null ? 0 : userRole.getRoleId());
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch user role.");
        }
    }

    @DeleteMapping("/delete-user/{roleId}/{userId}/{projectId}/{assignedUser}/")
    public ResponseEntity<String> deleteUserRole(@PathVariable int roleId,
                                                 @PathVariable String userId,
                                                 @PathVariable String projectId,
                                                 @PathVariable String assignedUser,
                                                 @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String googleId = authenticationService.getGoogleId(token);
            if (!adminService.checkAdmin(googleId)) {
                throw new UnauthorizedException("Only admins can delete users.");
            }

            UserRole userRole = repo.findByEmployeeIdAndProjectId(assignedUser, projectId);
            if (userRole == null || userRole.getRoleId() != roleId) {
                throw new ResourceNotFoundException("User role not found.");
            }

            repo.deleteByRoleIdAndEmployeeIdAndProjectId(roleId, assignedUser, projectId);
            return ResponseEntity.ok("User Deleted");

        } catch (ResourceNotFoundException | UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete user role.");
        }
    }
}
