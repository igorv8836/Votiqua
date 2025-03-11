package org.example.votiqua.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.example.votiqua.domain.model.poll.Poll
import org.example.votiqua.domain.model.poll.PollOption
import org.example.votiqua.domain.repository.PollRepository

class PollRepositoryImpl(
    private val dispatcher: CoroutineDispatcher
) : PollRepository {
    val polls = mutableListOf<Poll>(
        Poll(
            id = 1,
            title = "Poll Title",
            description = "Poll Description",
            options = listOf(
                PollOption(1, "Option 1", 0),
                PollOption(2, "Option 2", 0),
                PollOption(3, "Option 3", 0),
                PollOption(4, "Option 4", 0)
            )
        ),
        Poll(
            id = 2,
            title = "Poll Title 2",
            description = "Poll Description 2",
            options = listOf(
                PollOption(1, "Option 1", 0),
                PollOption(2, "Option 2", 0),
                PollOption(3, "Option 3", 0),
                PollOption(4, "Option 4", 0)
            )
        )
    )

    override suspend fun getPollById(pollId: Int): Poll? = withContext(dispatcher) {
        return@withContext polls.find { it.id == pollId }
    }

    override suspend fun updatePoll(poll: Poll) = withContext(dispatcher) {
        val index = polls.indexOfFirst { it.id == poll.id }
        if (index != -1) {
            polls[index] = poll
        }
    }

    override suspend fun deletePoll(pollId: Int): Unit = withContext(dispatcher) {
        polls.removeAll { it.id == pollId }
    }
}
