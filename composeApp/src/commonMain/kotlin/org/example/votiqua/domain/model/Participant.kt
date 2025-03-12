package org.example.votiqua.domain.model

data class Participant(
    val id: Int,
    val name: String,
    val avatarUrl: String?,
    val voted: Boolean,
    val selectedOption: String?
)
