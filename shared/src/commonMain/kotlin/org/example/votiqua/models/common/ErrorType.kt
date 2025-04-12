package org.example.votiqua.models.common

enum class ErrorType(val message: String) {
    INVALID_CREDENTIALS("Invalid credentials"),
    GENERAL("Something went wrong"),
    UNAUTHORIZED("Unauthorized"),
    FORBIDDEN("Forbidden"),
    INCORRECT_BODY("Incorrect body"),

    USER_ALREADY_EXISTS("User already exists"),
    USER_NOT_FOUND("User not found"),
    USERNAME_ALREADY_EXISTS("the name is already in use"),

    INVALID_EMAIL("Invalid email address"),


    POLL_NOT_FOUND("Poll not found"),

    PROFILE_NOT_FOUND("Profile not found"),
    FILE_TOO_LARGE_5MB("File too large, maximum - 5 MB"),
    INVALID_FILE_TYPE("Invalid file type, use jpeg, jpg or png"),
    PHOTO_NOT_FOUND("Photo not found"),
}