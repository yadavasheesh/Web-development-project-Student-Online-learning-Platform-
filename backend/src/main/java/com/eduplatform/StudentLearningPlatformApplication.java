package com.eduplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Main Spring Boot Application Class
 * Enables MongoDB auditing for automatic createdDate and lastModifiedDate
 */
@SpringBootApplication
@EnableMongoAuditing
@CrossOrigin(origins = "*", maxAge = 3600)
public class StudentLearningPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentLearningPlatformApplication.class, args);
    }
}