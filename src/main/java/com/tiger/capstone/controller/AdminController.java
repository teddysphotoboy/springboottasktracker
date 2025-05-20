package com.tiger.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tiger.capstone.service.AdminService;

@RestController
public class AdminController {
    @Autowired
    private AdminService service;
    @GetMapping("is_admin/{employeeId}")
    public boolean isAdmin(@PathVariable String employeeId){
        return service.checkAdmin(employeeId);
    }
}
