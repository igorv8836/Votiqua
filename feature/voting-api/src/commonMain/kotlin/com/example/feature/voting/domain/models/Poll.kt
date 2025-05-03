package com.example.feature.voting.domain.models

import androidx.compose.runtime.Stable

@Stable
data class Poll(
    val title: String,
    val endDate: String,
    val participants: Int,
    val status: String,
    val description: String,
    val category: String,
    val creationDate: String,
)