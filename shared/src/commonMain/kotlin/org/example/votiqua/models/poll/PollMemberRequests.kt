package org.example.votiqua.models.poll

import kotlinx.serialization.Serializable

@Serializable
data class LinkRequest(
    val link: String,
)

@Serializable
data class VoteRequest(
    val optionId: Int
)