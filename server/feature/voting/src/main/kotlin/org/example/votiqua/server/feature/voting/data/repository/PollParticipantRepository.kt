package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.auth.SimpleUser
import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.example.votiqua.server.feature.voting.database.PollParticipantTable
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PollParticipantRepository {
    fun insert(
        userId: Int,
        pollId: Int,
    ) {
        PollParticipantTable.insert {
            it[PollParticipantTable.pollId] = pollId
            it[PollParticipantTable.userId] = userId
            it[PollParticipantTable.createdAt] = currentDateTime()
        }
    }

    fun getPollParticipants(pollId: Int): List<SimpleUser> {
        return PollParticipantTable
            .innerJoin(UserTable, { PollParticipantTable.userId }, { UserTable.id })
            .select { PollParticipantTable.pollId eq pollId }
            .map {
                SimpleUser(
                    id = it[UserTable.id],
                    username = it[UserTable.username],
                    photoUrl = it[UserTable.photoUrl]
                )
            }
    }
//
//    private fun mapRowToPollParticipant(row: ResultRow): PollParticipant {
//        return PollParticipant(
//            user = SimpleUser(
//
//            ),
//        )
//    }
}