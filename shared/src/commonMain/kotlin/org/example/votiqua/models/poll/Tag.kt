package org.example.votiqua.models.poll

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: Int = 0,
    val name: String
) 