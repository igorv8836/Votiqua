package com.example.feature.voting.domain.models

data class Participant(
    val id: Int,
    val name: String,
    val avatarUrl: String?,
    val voted: Boolean,
    val selectedOption: String?
)