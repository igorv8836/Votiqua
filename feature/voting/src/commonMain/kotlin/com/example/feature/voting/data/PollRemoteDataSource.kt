package com.example.feature.voting.data

import com.example.votiqua.network.util.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.models.poll.PollsResponse
import org.example.votiqua.models.poll.RegenerateLinkResponse

class PollRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getMyPolls(limit: Int = 10, offset: Int = 0): Result<PollsResponse> {
        return safeApiCall {
            httpClient.get("polls/my-polls") {
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }
    }

    suspend fun getOtherPolls(limit: Int = 10, offset: Int = 0): Result<PollsResponse> {
        return safeApiCall {
            httpClient.get("polls/other-polls") {
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

    suspend fun deletePoll(pollId: Int): Result<Unit> {
        return safeApiCall {
            httpClient.delete("polls/$pollId")
        }
    }

    suspend fun startPoll(pollId: Int): Result<Unit> {
        return safeApiCall {
            httpClient.post("polls/$pollId/start")
        }
    }

    suspend fun regenerateLink(pollId: Int): Result<RegenerateLinkResponse> {
        return safeApiCall {
            httpClient.post("polls/$pollId/regenerate-link")
        }
    }

    suspend fun toggleFavorite(pollId: Int): Result<BaseResponse<String>> {
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