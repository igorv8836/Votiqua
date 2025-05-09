package com.example.feature.auth.domain

data class QueryRecommendModel(
    val query: String,
    val type: QueryType,
)


enum class QueryType {
    RECOMMEND,
    HISTORY
}