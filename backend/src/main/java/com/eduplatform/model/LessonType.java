package com.eduplatform.model;

/**
 * Lesson type enumeration
 */
public enum LessonType {
    VIDEO("Video"),
    TEXT("Text"),
    QUIZ("Quiz"),
    ASSIGNMENT("Assignment"),
    DOCUMENT("Document");

    private final String displayName;

    LessonType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}