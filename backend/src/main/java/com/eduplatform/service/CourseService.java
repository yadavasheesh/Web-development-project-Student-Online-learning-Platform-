package com.eduplatform.service;

import com.eduplatform.model.Course;
import com.eduplatform.model.CourseLevel;
import com.eduplatform.model.CourseStatus;
import com.eduplatform.repository.CourseRepository;
import com.eduplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

/**
 * Course Service for managing course operations
 * Implements business logic for course creation, management, and analytics
 */
@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new course
     */
    public Course createCourse(Course course) {
        // Set instructor name from user repository
        userRepository.findById(course.getInstructorId()).ifPresent(user -> {
            course.setInstructorName(user.getName());
        });

        course.setStatus(CourseStatus.DRAFT);
        course.setIsPublished(false);
        course.setRating(0.0);
        course.setEnrollmentCount(0);

        Course savedCourse = courseRepository.save(course);

        // Add course to instructor's created courses
        userRepository.findById(course.getInstructorId()).ifPresent(user -> {
            if (user.getCreatedCourses() == null) {
                user.setCreatedCourses(new java.util.ArrayList<>());
            }
            user.getCreatedCourses().add(savedCourse.getId());
            userRepository.save(user);
        });

        return savedCourse;
    }

    /**
     * Get course by ID
     */
    public Optional<Course> findById(String id) {
        return courseRepository.findById(id);
    }

    /**
     * Get all published courses
     */
    public Page<Course> getPublishedCourses(Pageable pageable) {
        return courseRepository.findPublishedCourses(pageable);
    }

    /**
     * Search courses with filters
     */
    public Page<Course> searchCourses(String searchTerm, String category, CourseLevel level, 
                                    Double minPrice, Double maxPrice, Pageable pageable) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return courseRepository.searchPublishedCourses(searchTerm, pageable);
        }

        if (category != null && level != null) {
            return courseRepository.findByCategoryAndLevel(category, level, pageable);
        }

        if (minPrice != null && maxPrice != null) {
            return courseRepository.findByPriceRange(minPrice, maxPrice, pageable);
        }

        return courseRepository.findPublishedCourses(pageable);
    }

    /**
     * Get courses by instructor
     */
    public Page<Course> getCoursesByInstructor(String instructorId, Pageable pageable) {
        return courseRepository.findByInstructorId(instructorId, pageable);
    }

    /**
     * Update course
     */
    public Course updateCourse(String id, Course courseDetails) {
        return courseRepository.findById(id).map(course -> {
            course.setTitle(courseDetails.getTitle());
            course.setDescription(courseDetails.getDescription());
            course.setCategory(courseDetails.getCategory());
            course.setLevel(courseDetails.getLevel());
            course.setPrice(courseDetails.getPrice());
            course.setDuration(courseDetails.getDuration());
            course.setImageUrl(courseDetails.getImageUrl());
            course.setSkills(courseDetails.getSkills());
            course.setLessons(courseDetails.getLessons());
            return courseRepository.save(course);
        }).orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    /**
     * Publish course
     */
    public Course publishCourse(String id) {
        return courseRepository.findById(id).map(course -> {
            course.setIsPublished(true);
            course.setStatus(CourseStatus.PUBLISHED);
            return courseRepository.save(course);
        }).orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    /**
     * Increment enrollment count
     */
    public Course incrementEnrollment(String courseId) {
        return courseRepository.findById(courseId).map(course -> {
            course.setEnrollmentCount(course.getEnrollmentCount() + 1);
            return courseRepository.save(course);
        }).orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
    }

    /**
     * Get course statistics
     */
    public Map<String, Object> getCourseStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCourses", courseRepository.count());
        stats.put("publishedCourses", courseRepository.countByStatus(CourseStatus.PUBLISHED));
        stats.put("draftCourses", courseRepository.countByStatus(CourseStatus.DRAFT));
        return stats;
    }

    /**
     * Get category statistics
     */
    public List<Object> getCategoryStatistics() {
        return courseRepository.getCategoryStatistics();
    }

    /**
     * Get free courses
     */
    public Page<Course> getFreeCourses(Pageable pageable) {
        return courseRepository.findFreeCourses(pageable);
    }

    /**
     * Delete course
     */
    public void deleteCourse(String id) {
        courseRepository.findById(id).map(course -> {
            course.setStatus(CourseStatus.ARCHIVED);
            return courseRepository.save(course);
        }).orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }
}