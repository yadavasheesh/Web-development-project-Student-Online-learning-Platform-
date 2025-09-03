package com.eduplatform.model;

/**
 * Question type enumeration for quizzes
 */
public enum QuestionType {
    MULTIPLE_CHOICE("Multiple Choice"),
    TRUE_FALSE("True/False"),
    TEXT("Text Answer"),
    NUMERIC("Numeric Answer");

    private final String displayName;

    QuestionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}