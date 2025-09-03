package com.eduplatform.repository;

import com.eduplatform.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Quiz Repository for managing course quizzes
 */
@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {

    // Course-based queries (indexed)
    List<Quiz> findByCourseId(String courseId);

    Page<Quiz> findByCourseId(String courseId, Pageable pageable);

    // Count quizzes per course
    long countByCourseId(String courseId);

    // Search quizzes by title
    @Query("{'title': {'$regex': ?0, '$options': 'i'}}")
    List<Quiz> findByTitleContainingIgnoreCase(String title);

    // Find quizzes with specific time limits
    List<Quiz> findByTimeLimitMinutesLessThanEqual(Integer timeLimit);

    List<Quiz> findByTimeLimitMinutesGreaterThanEqual(Integer timeLimit);

    // Find quizzes by passing score range
    List<Quiz> findByPassingScoreBetween(Integer minScore, Integer maxScore);

    // Find retakeable quizzes
    List<Quiz> findByAllowRetake(Boolean allowRetake);
}