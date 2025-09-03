package com.eduplatform.repository;

import com.eduplatform.model.Course;
import com.eduplatform.model.CourseLevel;
import com.eduplatform.model.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Course Repository with optimized queries for better performance
 * Uses compound indexes for efficient filtering and searching
 */
@Repository
public interface CourseRepository extends MongoRepository<Course, String> {

    // Basic queries with indexes
    List<Course> findByInstructorId(String instructorId);

    Page<Course> findByInstructorId(String instructorId, Pageable pageable);

    // Category-based queries (indexed)
    List<Course> findByCategory(String category);

    Page<Course> findByCategory(String category, Pageable pageable);

    // Level-based queries (indexed)
    List<Course> findByLevel(CourseLevel level);

    // Status-based queries (indexed)
    List<Course> findByStatus(CourseStatus status);

    Page<Course> findByStatus(CourseStatus status, Pageable pageable);

    // Published courses (most common query)
    @Query("{'isPublished': true, 'status': 'PUBLISHED'}")
    Page<Course> findPublishedCourses(Pageable pageable);

    // Combined queries using compound indexes
    List<Course> findByCategoryAndLevel(String category, CourseLevel level);

    Page<Course> findByCategoryAndLevel(String category, CourseLevel level, Pageable pageable);

    List<Course> findByInstructorIdAndCategory(String instructorId, String category);

    // Search queries with text indexing
    @Query("{'$and': [" +
           "{'$or': [" +
           "{'title': {'$regex': ?0, '$options': 'i'}}," +
           "{'description': {'$regex': ?0, '$options': 'i'}}," +
           "{'skills': {'$regex': ?0, '$options': 'i'}}" +
           "]}," +
           "{'isPublished': true}," +
           "{'status': 'PUBLISHED'}" +
           "]}")
    Page<Course> searchPublishedCourses(String searchTerm, Pageable pageable);

    // Advanced filtering with multiple criteria
    @Query("{'$and': [" +
           "{'isPublished': true}," +
           "{'status': 'PUBLISHED'}," +
           "{'category': {'$in': ?0}}," +
           "{'level': {'$in': ?1}}," +
           "{'price': {'$gte': ?2, '$lte': ?3}}" +
           "]}")
    Page<Course> findByMultipleCriteria(List<String> categories, List<CourseLevel> levels, 
                                       Double minPrice, Double maxPrice, Pageable pageable);

    // Statistical queries for dashboards
    long countByInstructorId(String instructorId);

    long countByCategory(String category);

    long countByStatus(CourseStatus status);

    // Top-rated courses
    @Query("{'isPublished': true, 'status': 'PUBLISHED'}")
    Page<Course> findTopRatedCourses(Pageable pageable);

    // Most enrolled courses
    @Query("{'isPublished': true, 'status': 'PUBLISHED'}")
    Page<Course> findMostEnrolledCourses(Pageable pageable);

    // Aggregation for category statistics
    @Aggregation(pipeline = {
        "{ '$match': { 'isPublished': true, 'status': 'PUBLISHED' } }",
        "{ '$group': { '_id': '$category', 'count': { '$sum': 1 }, 'avgRating': { '$avg': '$rating' } } }",
        "{ '$sort': { 'count': -1 } }"
    })
    List<Object> getCategoryStatistics();

    // Price range queries
    @Query("{'isPublished': true, 'status': 'PUBLISHED', 'price': {'$gte': ?0, '$lte': ?1}}")
    Page<Course> findByPriceRange(Double minPrice, Double maxPrice, Pageable pageable);

    // Free courses
    @Query("{'isPublished': true, 'status': 'PUBLISHED', '$or': [{'price': 0}, {'price': null}]}")
    Page<Course> findFreeCourses(Pageable pageable);
}