package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.feature.voting.database.PollAuthorTable
import org.jetbrains.exposed.sql.insert

class PollAuthorRepository {

    suspend fun addPollAuthor(
        createdPoll: Poll,
        userId: Int,
    ) {
        dbQuery {
            PollAuthorTable.insert {
                it[pollId] = createdPoll.id
                it[authorId] = userId
            }
        }
    }
}