package org.example.votiqua.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.example.votiqua.network.search.PollSearchResponse

class SearchRecommendRemoteDataSource(
    private val client: HttpClient,
) {
    suspend fun searchPolls(query: String, limit: Int = 10): PollSearchResponse {
        return client.get("api/v1/search") {
            parameter("query", query)
            parameter("limit", limit)
        }.body()
    }
}