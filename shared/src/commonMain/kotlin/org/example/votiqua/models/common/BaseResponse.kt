package org.example.votiqua.models.common

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val message: T
)