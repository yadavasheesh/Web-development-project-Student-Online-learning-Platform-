package com.eduplatform.controller;

import com.eduplatform.model.Course;
import com.eduplatform.model.CourseLevel;
import com.eduplatform.service.CourseService;
import com.eduplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;

/**
 * Course Controller
 * Handles course management REST API endpoints
 */
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    /**
     * Get all published courses with pagination
     */
    @GetMapping("/public")
    public ResponseEntity<Page<Course>> getPublishedCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Course> courses = courseService.getPublishedCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    /**
     * Search courses with filters
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Course>> searchCourses(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        CourseLevel courseLevel = null;
        if (level != null) {
            try {
                courseLevel = CourseLevel.valueOf(level.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid level, ignore
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseService.searchCourses(q, category, courseLevel, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(courses);
    }

    /**
     * Get course by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        Optional<Course> course = courseService.findById(id);
        return course.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create new course (Instructor/Admin only)
     */
    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course, Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<com.eduplatform.model.User> userOpt = userService.findByEmail(email);

            if (!userOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }

            com.eduplatform.model.User user = userOpt.get();
            course.setInstructorId(user.getId());

            Course savedCourse = courseService.createCourse(course);
            return ResponseEntity.ok(savedCourse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Enroll in course
     */
    @PostMapping("/{id}/enroll")
    public ResponseEntity<?> enrollInCourse(@PathVariable String id, Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<com.eduplatform.model.User> userOpt = userService.findByEmail(email);

            if (!userOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }

            com.eduplatform.model.User user = userOpt.get();
            userService.enrollInCourse(user.getId(), id);
            courseService.incrementEnrollment(id);

            return ResponseEntity.ok(Map.of("message", "Enrolled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Publish course
     */
    @PostMapping("/{id}/publish")
    public ResponseEntity<?> publishCourse(@PathVariable String id) {
        try {
            Course publishedCourse = courseService.publishCourse(id);
            return ResponseEntity.ok(publishedCourse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get course statistics (Admin only)
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCourseStatistics() {
        Map<String, Object> stats = courseService.getCourseStatistics();
        return ResponseEntity.ok(stats);
    }
}