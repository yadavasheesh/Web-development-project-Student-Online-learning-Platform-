package com.eduplatform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * User Entity for MongoDB
 * Optimized with indexes for better query performance
 */
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Indexed(unique = true) // Index for faster email queries
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Indexed // Index for role-based queries
    private UserRole role;

    private String avatar;
    private String bio;
    private String phoneNumber;

    @Indexed // Index for status queries
    private UserStatus status = UserStatus.ACTIVE;

    // Course relationships
    private List<String> enrolledCourses;
    private List<String> completedCourses;
    private List<String> createdCourses; // For instructors
    private List<String> certificates;

    // Progress tracking
    private Map<String, Double> courseProgress; // courseId -> progress percentage

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Constructors
    public User() {}

    public User(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public List<String> getEnrolledCourses() { return enrolledCourses; }
    public void setEnrolledCourses(List<String> enrolledCourses) { this.enrolledCourses = enrolledCourses; }

    public List<String> getCompletedCourses() { return completedCourses; }
    public void setCompletedCourses(List<String> completedCourses) { this.completedCourses = completedCourses; }

    public List<String> getCreatedCourses() { return createdCourses; }
    public void setCreatedCourses(List<String> createdCourses) { this.createdCourses = createdCourses; }

    public List<String> getCertificates() { return certificates; }
    public void setCertificates(List<String> certificates) { this.certificates = certificates; }

    public Map<String, Double> getCourseProgress() { return courseProgress; }
    public void setCourseProgress(Map<String, Double> courseProgress) { this.courseProgress = courseProgress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}