package org.example.votiqua.models.poll

import kotlinx.serialization.Serializable
import org.example.votiqua.models.auth.SimpleUser

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
    val totalVotes: Int = 0,
    val authorId: Int = 0,
    val isFavorite: Boolean = false,
    val members: List<SimpleUser> = emptyList(),
) 