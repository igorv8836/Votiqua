package org.example.votiqua.data.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.example.votiqua.models.search.PollSearchResponse

class SearchRecommendRemoteDataSource(
    private val client: HttpClient,
) {
    private var _lastResponse: PollSearchResponse? = null
    val lastResponse: PollSearchResponse?
        get() = _lastResponse

    suspend fun searchPolls(query: String, limit: Int = 10): PollSearchResponse {
        val answer = client.get("api/v1/search") {
            parameter("query", query)
            parameter("limit", limit)
        }.body<PollSearchResponse>()
        _lastResponse = answer

        return answer
    }
}