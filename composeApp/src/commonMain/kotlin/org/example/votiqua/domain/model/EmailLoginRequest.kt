package org.example.votiqua.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginRequest(
    val email: String,
    val password: String
)