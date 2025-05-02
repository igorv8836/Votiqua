package org.example.votiqua.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class PasswordRecoveryRequest(
    val email: String
)

@Serializable
data class PasswordResetRequest(
    val email: String,
    val code: Int,
    val newPassword: String
)

@Serializable
data class PasswordChangeRequest(
    val lastPassword: String,
    val newPassword: String,
)