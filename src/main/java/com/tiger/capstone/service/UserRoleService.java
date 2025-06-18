package com.tiger.capstone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tiger.capstone.dto.UserRoleDTO;
import com.tiger.capstone.exception.DatabaseException;
import com.tiger.capstone.model.UserRole;
import com.tiger.capstone.repository.UserRoleRepository;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository repo;
    
    public String createUserRole(String userId, UserRoleDTO dto){
        try {
            UserRole existing = repo.findByEmployeeIdAndProjectId(dto.getEmployeeId(), dto.getProjectId());
            if (existing != null) {
                return "User already exists";
            }

            UserRole role = new UserRole();
            role.setEmployeeId(dto.getEmployeeId());
            role.setProjectId(dto.getProjectId());
            role.setRoleId(dto.getRoleId());

            repo.save(role);
            return "Success";
        } catch (Exception e) {
            throw new DatabaseException("Error creating user role.");
        }
    }
}
