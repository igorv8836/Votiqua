package org.example.votiqua.network.search.common

enum class ErrorType(val message: String) {
    INVALID_CREDENTIALS("Invalid credentials"),
    GENERAL("Something went wrong"),
    UNAUTHORIZED("Unauthorized"),
    FORBIDDEN("Forbidden"),
}