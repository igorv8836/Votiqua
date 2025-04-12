package org.example.votiqua.models.poll

import kotlinx.serialization.Serializable

@Serializable
data class PollOption(
    val id: Int,
    val pollId: Int,
    val optionText: String,
    val orderIndex: Int = 0,
    val voteCount: Int = 0,
    val percentage: Double = 0.0,
    val isSelected: Boolean = false
) 