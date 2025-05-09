package org.example.votiqua.models.poll

import kotlinx.serialization.Serializable

@Serializable
data class Poll(
    val id: Int,
    val question: String,
    val description: String? = null,
    val isMultiple: Boolean = false,
    val isAnonymous: Boolean = false,
    val isOpen: Boolean = false,
    val createdAt: Long,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val options: List<PollOption> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val authorId: Int = 0,
    val members: List<PollParticipant> = emptyList(),
    val isStarted: Boolean = false,
    val link: String? = null,

    val context: PollContext = PollContext(),
)

@Serializable
data class PollContext(
    val isFavorite: Boolean = false,
    val isAdmin: Boolean = false,
    val selectedOption: Int? = null,
    val totalVotes: Int = 0,
    val memberCount: Int = 0,
)