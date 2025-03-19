package com.example.votiqua.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val success: Boolean,
    val message: T
)