package com.eduplatform.model;

/**
 * User Role enumeration
 */
public enum UserRole {
    STUDENT("Student"),
    INSTRUCTOR("Instructor"), 
    ADMIN("Administrator");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}