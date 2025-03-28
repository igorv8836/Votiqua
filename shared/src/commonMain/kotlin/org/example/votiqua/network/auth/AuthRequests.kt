package org.example.votiqua.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
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
    val token: String,
    val newPassword: String
)