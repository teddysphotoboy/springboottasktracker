package com.tiger.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tiger.capstone.service.AdminService;
import com.tiger.capstone.service.AuthenticationService;
import com.tiger.capstone.exception.UnauthorizedException;

@RestController
public class AdminController {
    @Autowired
    private AdminService service;

    @Autowired 
    private AuthenticationService authenticationService;

    @GetMapping("is_admin/{employeeId}")
    public boolean isAdmin(@PathVariable String employeeId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String googleId = authenticationService.getGoogleId(token);
            return service.checkAdmin(googleId);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid or missing token.");
        }
    }
}
