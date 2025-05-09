package com.example.feature.voting.domain.models

import androidx.compose.runtime.Stable

@Stable
data class PollCardState(
    val title: String,
    val endDate: String,
    val participants: Int,
    val status: String,
    val description: String,
    val category: String,
    val creationDate: String,
    val isFavorite: Boolean,
    val id: Int = -1,
)