package com.tiger.capstone.controller;

import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tiger.capstone.dto.TokenRequestDTO;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
public class AuthController {

    private static final String CLIENT_ID = "714826473195-mkbur5p2vel0mtc8o8suvr94rdni8n4g.apps.googleusercontent.com";

    @PostMapping("/authenticate")
    public String authenticateUser(@RequestBody TokenRequestDTO request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
            System.out.println("token"+request.getToken());
            GoogleIdToken idToken = verifier.verify(request.getToken());
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                return "Authentication successful for user: "+userId+" " + name + " (" + email + ")";
            } else {
                return "Invalid ID token.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Authentication failed: " + e.getMessage();
        }
    }
}

