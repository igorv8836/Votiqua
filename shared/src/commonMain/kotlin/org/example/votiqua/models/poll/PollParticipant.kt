package org.example.votiqua.models.poll

import kotlinx.serialization.Serializable
import org.example.votiqua.models.auth.SimpleUser

@Serializable
data class PollParticipant(
    val user: SimpleUser,
    val voted: Boolean = false,
    val optionId: Int? = null,
    val optionText: String? = null
)