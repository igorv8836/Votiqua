package org.example.votiqua.server.feature.auth.domain.models

data class PasswordResetModel(
    val email: String,
    val code: Int,
    val createdAt: Long,
    val countInputAttempts: Int,
    val isUsed: Boolean
)