package com.example.feature.auth.data.network

import com.example.votiqua.network.util.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.example.votiqua.models.recom.MainScreenResponse
import org.example.votiqua.models.search.PollSearchResponse
import org.example.votiqua.models.search.PollTitlesSearchResponse

class RecomRemoteDataSource(
    private val httpClient: HttpClient,
) {
    suspend fun getPollTitles(query: String, limit: Int = 10): Result<PollTitlesSearchResponse> = safeApiCall {
        val answer = httpClient.get("search") {
            parameter("query", query)
            parameter("limit", limit)
        }

        return@safeApiCall answer
    }

    suspend fun getPolls(query: String, limit: Int = 10): Result<PollSearchResponse> {
        return safeApiCall {
            httpClient.get("search/polls") {
                parameter("query", query)
                parameter("limit", limit)
            }
        }
    }

    suspend fun getMainScreenPolls(): Result<MainScreenResponse> {
        return safeApiCall {
            httpClient.get("main")
        }
    }
}