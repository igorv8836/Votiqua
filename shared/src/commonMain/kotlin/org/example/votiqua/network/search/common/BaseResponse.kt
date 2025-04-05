package org.example.votiqua.network.search.common

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val message: T
)