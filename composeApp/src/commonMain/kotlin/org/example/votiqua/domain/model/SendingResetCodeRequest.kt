package org.example.votiqua.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SendingResetCodeRequest(
    val email: String
)