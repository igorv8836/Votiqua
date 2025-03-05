package org.example.votiqua.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequest(
    val email: String,
    val code: Int,
    val newPassword: String
)