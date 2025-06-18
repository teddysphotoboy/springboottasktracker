package com.tiger.capstone.controller;

import com.tiger.capstone.dto.TokenRequestDTO;
import com.tiger.capstone.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public String authenticateUser(@RequestBody TokenRequestDTO request) {
        String userId = authenticationService.getGoogleId(request.getToken());
        return "Authentication successful for user: " + userId;
    }
}
