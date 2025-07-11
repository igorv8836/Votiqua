package org.example.votiqua.models.search

import kotlinx.serialization.Serializable
import org.example.votiqua.models.poll.Poll

@Serializable
data class PollSearchResponse(
    val query: String,
    val count: Int,
    val results: List<Poll>
) 