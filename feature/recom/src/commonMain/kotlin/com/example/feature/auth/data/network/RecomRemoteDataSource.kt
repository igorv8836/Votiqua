package com.example.feature.auth.data.network

import com.example.votiqua.network.util.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.example.votiqua.models.recom.MainScreenResponse
import org.example.votiqua.models.search.PollSearchResponse

class RecomRemoteDataSource(
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

    suspend fun getMainScreenPolls(): Result<MainScreenResponse> {
        return safeApiCall {
            client.get("main")
        }
    }
}