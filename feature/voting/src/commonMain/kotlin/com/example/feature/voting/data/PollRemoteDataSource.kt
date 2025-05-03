package com.example.feature.voting.data

import com.example.feature.voting.domain.models.PollListResponse
import com.example.votiqua.network.util.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.models.poll.Poll

class PollRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getPolls(limit: Int = 10, offset: Int = 0): Result<PollListResponse> {
        return safeApiCall {
            httpClient.get("polls/my-polls") {
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }
    }

    suspend fun getPoll(id: Int): Result<Poll> {
        return safeApiCall {
            httpClient.get("polls/$id")
        }
    }

    suspend fun createPoll(poll: Poll): Result<Poll> {
        return safeApiCall {
            httpClient.post("polls") {
                setBody(poll)
            }
        }
    }

    suspend fun updatePoll(poll: Poll): Result<Poll> {
        return safeApiCall {
            httpClient.post("polls/edit") {
                setBody(poll)
            }
        }
    }

    suspend fun getUserPolls(userId: Int, limit: Int = 10, offset: Int = 0): Result<PollListResponse> {
        return safeApiCall {
            httpClient.get("polls/user/$userId") {
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }
    }

    suspend fun toggleFavorite(pollId: Int): Result<BaseResponse<String>> { //  TODO
        return safeApiCall {
            httpClient.post("favorites/$pollId")
        }
    }

    suspend fun getFavorites(): Result<List<Poll>> {
        return safeApiCall {
            httpClient.get("favorites")
        }
    }

    suspend fun vote(pollId: Int, optionId: Int): Result<Poll> {
        return safeApiCall {
            httpClient.post("polls/$pollId/vote") {
                parameter("optionId", optionId)
            }
        }
    }
}