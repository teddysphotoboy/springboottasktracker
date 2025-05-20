package com.tiger.capstone.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tiger.capstone.dto.UserRoleDTO;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.UserRoleRepository;
import com.tiger.capstone.service.AdminService;
import com.tiger.capstone.service.UserRoleService;

@RestController
public class UserRoleController {
    @Autowired
    private UserRoleService service;

    @Autowired 
    private UserRoleRepository repo;

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-user-role/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createUserRole(@PathVariable String userId, @RequestBody UserRoleDTO dto) {
        String message = service.createUserRole(userId, dto);
        return Map.of("detail", message);
    }

    @GetMapping("/user-role/{userId}/{projectId}")
    public ResponseEntity<Integer> getUserRoleByProject(
        @PathVariable String userId,
        @PathVariable String projectId) {
    
    UserRole userRole = repo.findByEmployeeIdAndProjectId(userId, projectId);

    return ResponseEntity.ok(userRole==null?0:userRole.getRoleId());
    }

    @DeleteMapping("/delete-user/{roleId}/{userId}/{projectId}/{assignedUser}/")
    public ResponseEntity<String> deleteUserRole(@PathVariable int roleId,@PathVariable String userId,@PathVariable String projectId,@PathVariable String assignedUser){
        if(adminService.checkAdmin(userId)){

            repo.deleteByRoleIdAndEmployeeIdAndProjectId(roleId, assignedUser, projectId);
            return ResponseEntity.ok("User Deleted");
        }
        return ResponseEntity.ok("Access Denied");
    }
}
