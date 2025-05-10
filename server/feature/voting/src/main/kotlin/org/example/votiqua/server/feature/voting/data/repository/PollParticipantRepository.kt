package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.auth.SimpleUser
import org.example.votiqua.models.poll.PollParticipant
import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.example.votiqua.server.feature.profile.data.ProfilePhotoUrlConverter
import org.example.votiqua.server.feature.voting.database.PollOptionTable
import org.example.votiqua.server.feature.voting.database.PollParticipantTable
import org.example.votiqua.server.feature.voting.database.VoteTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PollParticipantRepository(
    private val profilePhotoUrlConverter: ProfilePhotoUrlConverter,
) {
    fun insert(
        userId: Int,
        pollId: Int,
    ) {
        val exists = PollParticipantTable
            .select {
                (PollParticipantTable.pollId eq pollId) and
                        (PollParticipantTable.userId eq userId)
            }
            .count() > 0

        if (!exists) {
            PollParticipantTable.insert {
                it[PollParticipantTable.pollId] = pollId
                it[PollParticipantTable.userId] = userId
                it[PollParticipantTable.createdAt] = currentDateTime()
            }
        }
    }

    fun getPollParticipants(pollId: Int): List<PollParticipant> {
        val query = UserTable
            .join(
                PollParticipantTable,
                JoinType.INNER,
                additionalConstraint = { UserTable.id eq PollParticipantTable.userId }
            )
            .select { PollParticipantTable.pollId eq pollId }

        val users = query.map { row ->
            val userId = row[UserTable.id]

            val photoUrl = if (row[UserTable.photoUrl] != null)
                profilePhotoUrlConverter.getUserPhotoUrl(row[UserTable.id])
            else
                null

            val user = SimpleUser(
                id = userId,
                username = row[UserTable.username],
                photoUrl = photoUrl,
            )

            val voteQuery = VoteTable
                .join(
                    PollOptionTable,
                    JoinType.LEFT,
                    additionalConstraint = { VoteTable.optionId eq PollOptionTable.id }
                )
                .select {
                    (VoteTable.pollId eq pollId) and (VoteTable.userId eq userId)
                }
                .limit(1)

            val voteRow = voteQuery.firstOrNull()
            if (voteRow != null) {
                PollParticipant(
                    user = user,
                    voted = true,
                    optionId = voteRow[VoteTable.optionId],
                    optionText = voteRow[PollOptionTable.optionText]
                )
            } else {
                PollParticipant(user = user)
            }
        }

        return users
    }

    suspend fun removeMember(pollId: Int, userId: Int) {
        dbQuery {
            VoteTable.deleteWhere {
                (VoteTable.pollId eq pollId) and (VoteTable.userId eq userId)
            }
            
            PollParticipantTable.deleteWhere {
                (PollParticipantTable.pollId eq pollId) and (PollParticipantTable.userId eq userId)
            }
        }
    }
}