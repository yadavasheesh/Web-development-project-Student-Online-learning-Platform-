package com.eduplatform.repository;

import com.eduplatform.model.User;
import com.eduplatform.model.UserRole;
import com.eduplatform.model.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository with optimized queries
 * Extends MongoRepository for basic CRUD operations
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Basic finder methods
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    // Role-based queries with optimized indexes
    List<User> findByRole(UserRole role);

    Page<User> findByRole(UserRole role, Pageable pageable);

    // Status-based queries
    List<User> findByStatus(UserStatus status);

    // Combined queries for better performance
    List<User> findByRoleAndStatus(UserRole role, UserStatus status);

    // Name search with case-insensitive matching
    @Query("{'name': {'$regex': ?0, '$options': 'i'}}")
    List<User> findByNameContainingIgnoreCase(String name);

    // Email search with case-insensitive matching
    @Query("{'email': {'$regex': ?0, '$options': 'i'}}")
    List<User> findByEmailContainingIgnoreCase(String email);

    // Count queries for dashboard statistics
    long countByRole(UserRole role);

    long countByStatus(UserStatus status);

    // Advanced search with multiple criteria
    @Query("{'$and': [" +
           "{'$or': [{'name': {'$regex': ?0, '$options': 'i'}}, {'email': {'$regex': ?0, '$options': 'i'}}]}," +
           "{'role': ?1}," +
           "{'status': ?2}" +
           "]}")
    Page<User> findBySearchTermAndRoleAndStatus(String searchTerm, UserRole role, UserStatus status, Pageable pageable);

    // Enrollment-based queries
    @Query("{'enrolledCourses': ?0}")
    List<User> findByEnrolledCoursesContaining(String courseId);

    @Query("{'completedCourses': ?0}")
    List<User> findByCompletedCoursesContaining(String courseId);

    // Instructor queries
    @Query("{'createdCourses': ?0}")
    List<User> findByCreatedCoursesContaining(String courseId);

    Page<User> findByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable);
}