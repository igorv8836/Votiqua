package org.example.votiqua.domain.model.search

data class QueryRecommendModel(
    val query: String,
    val type: QueryType,
)


enum class QueryType {
    RECOMMEND,
    HISTORY
}