package com.tiger.capstone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiger.capstone.model.UserRole;

import jakarta.transaction.Transactional;

public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {
    UserRole findByEmployeeIdAndProjectId(String employeeId, String projectId);
    List<UserRole> findByEmployeeId(String employeeId);
    List<UserRole> findByProjectId(String projectId);

    @Transactional
    void deleteByRoleIdAndEmployeeIdAndProjectId(int roleId,String employeeId,String projectId);

    @Transactional
    void deleteByProjectId(String projectId);
    
} 
