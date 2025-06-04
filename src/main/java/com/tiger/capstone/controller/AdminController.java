package com.tiger.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tiger.capstone.service.AdminService;
import com.tiger.capstone.service.AuthenticationService;

@RestController
public class AdminController {
    @Autowired
    private AdminService service;

    @Autowired 
    private AuthenticationService authenticationService;

    @GetMapping("is_admin/{employeeId}")
    public boolean isAdmin(@PathVariable String employeeId,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String googleId=authenticationService.getGoogleId(token);
        return service.checkAdmin(googleId);
    }
}
