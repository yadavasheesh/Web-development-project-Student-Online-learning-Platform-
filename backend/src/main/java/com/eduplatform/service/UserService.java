package com.eduplatform.service;

import com.eduplatform.model.User;
import com.eduplatform.model.UserRole;
import com.eduplatform.model.UserStatus;
import com.eduplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

/**
 * User Service for managing user operations
 * Implements business logic for user management, enrollment, and progress tracking
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create a new user with encrypted password
     */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    /**
     * Find user by email for authentication
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get user by ID
     */
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Update user profile
     */
    public User updateUser(String id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setBio(userDetails.getBio());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setAvatar(userDetails.getAvatar());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * Get all users with pagination
     */
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Get users by role
     */
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * Search users with filters
     */
    public Page<User> searchUsers(String searchTerm, UserRole role, UserStatus status, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            if (role != null && status != null) {
                return userRepository.findByRoleAndStatus(role, status, pageable);
            } else if (role != null) {
                return userRepository.findByRole(role, pageable);
            } else {
                return userRepository.findAll(pageable);
            }
        }
        return userRepository.findBySearchTermAndRoleAndStatus(searchTerm, role, status, pageable);
    }

    /**
     * Enroll user in course
     */
    public User enrollInCourse(String userId, String courseId) {
        return userRepository.findById(userId).map(user -> {
            if (user.getEnrolledCourses() != null && user.getEnrolledCourses().contains(courseId)) {
                throw new RuntimeException("User already enrolled in this course");
            }

            if (user.getEnrolledCourses() == null) {
                user.setEnrolledCourses(new java.util.ArrayList<>());
            }
            user.getEnrolledCourses().add(courseId);

            if (user.getCourseProgress() == null) {
                user.setCourseProgress(new HashMap<>());
            }
            user.getCourseProgress().put(courseId, 0.0);

            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    /**
     * Update course progress for user
     */
    public User updateCourseProgress(String userId, String courseId, double progress) {
        return userRepository.findById(userId).map(user -> {
            if (user.getCourseProgress() == null) {
                user.setCourseProgress(new HashMap<>());
            }
            user.getCourseProgress().put(courseId, progress);

            // Mark course as completed if progress is 100%
            if (progress >= 100.0) {
                if (user.getCompletedCourses() == null) {
                    user.setCompletedCourses(new java.util.ArrayList<>());
                }
                if (!user.getCompletedCourses().contains(courseId)) {
                    user.getCompletedCourses().add(courseId);
                }
            }

            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    /**
     * Get user statistics
     */
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalStudents", userRepository.countByRole(UserRole.STUDENT));
        stats.put("totalInstructors", userRepository.countByRole(UserRole.INSTRUCTOR));
        stats.put("totalAdmins", userRepository.countByRole(UserRole.ADMIN));
        stats.put("activeUsers", userRepository.countByStatus(UserStatus.ACTIVE));
        return stats;
    }

    /**
     * Delete user (soft delete by changing status)
     */
    public void deleteUser(String id) {
        userRepository.findById(id).map(user -> {
            user.setStatus(UserStatus.INACTIVE);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * Validate password for authentication
     */
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}