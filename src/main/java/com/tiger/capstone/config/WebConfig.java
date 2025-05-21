package com.tiger.capstone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")            // allow all origins
            .allowedMethods("*")            // allow all HTTP methods (GET, POST, etc.)
            .allowedHeaders("*")            // allow all headers
            .allowCredentials(true)         // allow credentials (cookies, auth headers)
            .maxAge(3600);
    }
}
