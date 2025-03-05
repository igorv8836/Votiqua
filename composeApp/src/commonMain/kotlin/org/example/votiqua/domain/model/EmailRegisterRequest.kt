package org.example.votiqua.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailRegisterRequest(
    val email: String,
    val password: String,
    val username: String
)