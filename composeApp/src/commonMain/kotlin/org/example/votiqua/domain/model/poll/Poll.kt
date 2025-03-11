package org.example.votiqua.domain.model.poll

data class Poll(
    val id: Int,
    val title: String,
    val description: String?,
    val options: List<PollOption>
)

data class PollOption(
    val id: Int,
    val text: String,
    val votes: Int
)