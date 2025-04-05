package org.example.votiqua.server.feature.auth.domain.models

import java.time.LocalDateTime

data class PasswordResetModel(
    val email: String,
    val code: Int,
    val createdAt: LocalDateTime,
    val countInputAttempts: Int,
    val isUsed: Boolean
)