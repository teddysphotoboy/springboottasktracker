package com.tiger.capstone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tiger.capstone.repository.AdminRepository;

@Service
public class AdminService {
    @Autowired
    private AdminRepository repo;

    public boolean checkAdmin(String id){
        return repo.existsByEmployeeId(id);
    }

}
