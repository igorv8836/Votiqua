package com.example.feature.voting.domain.models

import androidx.compose.runtime.Stable

@Stable
data class Participant(
    val id: Int,
    val name: String,
    val avatarUrl: String?,
    val voted: Boolean,
    val selectedOption: String?
)