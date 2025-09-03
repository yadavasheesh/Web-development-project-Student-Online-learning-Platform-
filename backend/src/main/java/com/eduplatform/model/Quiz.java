package com.eduplatform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Quiz Entity for MongoDB
 */
@Document(collection = "quizzes")
public class Quiz {

    @Id
    private String id;

    @NotBlank(message = "Quiz title is required")
    private String title;

    private String description;

    @NotBlank(message = "Course ID is required")
    @Indexed // For course-based queries
    private String courseId;

    private List<Question> questions;

    @Min(value = 1, message = "Time limit must be at least 1 minute")
    private Integer timeLimitMinutes;

    @Min(value = 0, message = "Passing score cannot be negative")
    @Max(value = 100, message = "Passing score cannot exceed 100")
    private Integer passingScore = 70;

    private Boolean allowRetake = true;
    private Integer maxAttempts = 3;
    private Boolean shuffleQuestions = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Nested Question class
    public static class Question {
        private String id;
        private String question;
        private QuestionType type;
        private List<String> options;
        private String correctAnswer; // For text/numeric answers
        private Integer correctAnswerIndex; // For multiple choice
        private Boolean correctAnswerBoolean; // For true/false
        private String explanation;
        private Integer points = 1;

        // Constructors
        public Question() {}

        public Question(String question, QuestionType type) {
            this.question = question;
            this.type = type;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public QuestionType getType() { return type; }
        public void setType(QuestionType type) { this.type = type; }

        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }

        public String getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

        public Integer getCorrectAnswerIndex() { return correctAnswerIndex; }
        public void setCorrectAnswerIndex(Integer correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }

        public Boolean getCorrectAnswerBoolean() { return correctAnswerBoolean; }
        public void setCorrectAnswerBoolean(Boolean correctAnswerBoolean) { this.correctAnswerBoolean = correctAnswerBoolean; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }

        public Integer getPoints() { return points; }
        public void setPoints(Integer points) { this.points = points; }
    }

    // Constructors
    public Quiz() {}

    public Quiz(String title, String courseId) {
        this.title = title;
        this.courseId = courseId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public Integer getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(Integer timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }

    public Integer getPassingScore() { return passingScore; }
    public void setPassingScore(Integer passingScore) { this.passingScore = passingScore; }

    public Boolean getAllowRetake() { return allowRetake; }
    public void setAllowRetake(Boolean allowRetake) { this.allowRetake = allowRetake; }

    public Integer getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(Integer maxAttempts) { this.maxAttempts = maxAttempts; }

    public Boolean getShuffleQuestions() { return shuffleQuestions; }
    public void setShuffleQuestions(Boolean shuffleQuestions) { this.shuffleQuestions = shuffleQuestions; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}