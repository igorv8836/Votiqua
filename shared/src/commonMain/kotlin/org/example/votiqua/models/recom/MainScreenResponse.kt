package org.example.votiqua.models.recom

import kotlinx.serialization.Serializable
import org.example.votiqua.models.poll.Poll

@Serializable
data class MainScreenResponse(
    val newPolls: List<Poll>,
    val popularPolls: List<Poll>,
    val adminMessages: List<String>,
)