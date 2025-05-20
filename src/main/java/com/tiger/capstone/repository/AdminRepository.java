package com.tiger.capstone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiger.capstone.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer> {

    boolean existsByEmployeeId(String employeeId);
    
} 
