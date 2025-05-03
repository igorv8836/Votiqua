package org.example.votiqua.models.poll

import kotlinx.serialization.Serializable

@Serializable
data class PollsResponse(
    val poll: List<Poll>,
)