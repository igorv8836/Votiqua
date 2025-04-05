package org.example.votiqua.models.search

import kotlinx.serialization.Serializable

@Serializable
data class PollSearchResponse(
    val query: String,
    val count: Int,
    val results: List<String>
)