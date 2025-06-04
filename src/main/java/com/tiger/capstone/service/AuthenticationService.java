package com.tiger.capstone.service;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Service
public class AuthenticationService {
    private static final String CLIENT_ID = "714826473195-mkbur5p2vel0mtc8o8suvr94rdni8n4g.apps.googleusercontent.com";
    public String getGoogleId(String token){
            try {
                        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                                GoogleNetHttpTransport.newTrustedTransport(),
                                JacksonFactory.getDefaultInstance())
                            .setAudience(Collections.singletonList(CLIENT_ID))
                            .build();
                        GoogleIdToken idToken = verifier.verify(token);
                        if (idToken != null) {
                            Payload payload = idToken.getPayload();
                            String userId = payload.getSubject();


                            return userId;
                        } else {
                            return "Invalid ID token.";
                        }
            } catch (Exception e) {
                e.printStackTrace();
                return "Authentication failed: " + e.getMessage();
            }
    }
}
