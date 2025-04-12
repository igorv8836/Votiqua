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
    val startDate: Long? = null,
    val endDate: Long? = null,
    val options: List<PollOption> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val totalVotes: Int = 0,
    val authorId: Int? = null,
    val authorName: String? = null
) 