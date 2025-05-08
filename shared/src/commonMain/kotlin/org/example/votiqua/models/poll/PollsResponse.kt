package org.example.votiqua.models.poll

import kotlinx.serialization.Serializable

@Serializable
data class PollsResponse(
    val polls: List<Poll>,
)

@Serializable
data class RegenerateLinkResponse(
    val link: String,
    val pollId: Int,
)