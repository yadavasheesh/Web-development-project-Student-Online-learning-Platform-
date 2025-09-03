package com.eduplatform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Course Entity for MongoDB with optimized indexes
 */
@Document(collection = "courses")
@CompoundIndexes({
    @CompoundIndex(name = "category_level_idx", def = "{'category': 1, 'level': 1}"),
    @CompoundIndex(name = "instructor_category_idx", def = "{'instructorId': 1, 'category': 1}")
})
public class Course {

    @Id
    private String id;

    @NotBlank(message = "Course title is required")
    @Indexed // For text search optimization
    private String title;

    @NotBlank(message = "Course description is required")
    private String description;

    @NotBlank(message = "Instructor ID is required")
    @Indexed // For instructor queries
    private String instructorId;

    private String instructorName;

    @NotBlank(message = "Category is required")
    @Indexed // For category filtering
    private String category;

    @NotNull(message = "Course level is required")
    @Indexed // For level filtering
    private CourseLevel level;

    private String duration;

    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    @Min(value = 0, message = "Rating cannot be negative")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Double rating = 0.0;

    private Integer enrollmentCount = 0;
    private String imageUrl;
    private List<String> skills;
    private List<Lesson> lessons;

    @Indexed // For status queries
    private CourseStatus status = CourseStatus.DRAFT;

    // Course settings
    private Boolean isPublished = false;
    private Boolean allowCertification = true;
    private Integer passingScore = 70;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Nested Lesson class
    public static class Lesson {
        private String id;
        private String title;
        private String description;
        private String duration;
        private LessonType type;
        private String content;
        private String videoUrl;
        private String quizId;
        private Integer order;

        // Constructors
        public Lesson() {}

        public Lesson(String title, LessonType type, String content) {
            this.title = title;
            this.type = type;
            this.content = content;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public LessonType getType() { return type; }
        public void setType(LessonType type) { this.type = type; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getVideoUrl() { return videoUrl; }
        public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

        public String getQuizId() { return quizId; }
        public void setQuizId(String quizId) { this.quizId = quizId; }

        public Integer getOrder() { return order; }
        public void setOrder(Integer order) { this.order = order; }
    }

    // Constructors
    public Course() {}

    public Course(String title, String description, String instructorId, String category) {
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.category = category;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public CourseLevel getLevel() { return level; }
    public void setLevel(CourseLevel level) { this.level = level; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getEnrollmentCount() { return enrollmentCount; }
    public void setEnrollmentCount(Integer enrollmentCount) { this.enrollmentCount = enrollmentCount; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }

    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }

    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }

    public Boolean getAllowCertification() { return allowCertification; }
    public void setAllowCertification(Boolean allowCertification) { this.allowCertification = allowCertification; }

    public Integer getPassingScore() { return passingScore; }
    public void setPassingScore(Integer passingScore) { this.passingScore = passingScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}