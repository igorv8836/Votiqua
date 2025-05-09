package com.example.feature.voting.data

import com.example.votiqua.network.util.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.example.votiqua.models.poll.LinkRequest
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.models.poll.VoteRequest

class PollMemberRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun vote(pollId: Int, optionId: Int): Result<Unit> {
        return safeApiCall {
            httpClient.post("poll-member/$pollId/vote") {
                setBody(VoteRequest(optionId))
            }
        }
    }

    suspend fun leavePoll(pollId: Int): Result<Unit> {
        return safeApiCall {
            httpClient.delete("poll-member/$pollId/leave")
        }
    }

    suspend fun joinByLink(link: String): Result<Poll> {
        return safeApiCall {
            httpClient.post("poll-member/join-by-link") {
                setBody(LinkRequest(link))
            }
        }
    }
} 