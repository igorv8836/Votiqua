package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.feature.voting.database.FavoritePollTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PollFavoriteRepository {
    suspend fun changeFavoriteStatus(
        userId: Int,
        pollId: Int,
    ): Boolean {
        return dbQuery {
            if (hasFavoritePoll(userId, pollId)) {
                FavoritePollTable.deleteWhere {
                    (FavoritePollTable.pollId eq pollId) and (FavoritePollTable.userId eq userId)
                }
            } else {
                FavoritePollTable.insert {
                    it[FavoritePollTable.pollId] = pollId
                    it[FavoritePollTable.userId] = userId
                    it[FavoritePollTable.createdAt] = currentDateTime()
                }
            }
            return@dbQuery true
        }
    }

    private fun hasFavoritePoll(
        userId: Int,
        pollId: Int,
    ): Boolean {
        return FavoritePollTable.select {
            (FavoritePollTable.pollId eq pollId) and (FavoritePollTable.userId eq userId)
        }.count() > 0
    }

    suspend fun getFavoritePollsIds(
        userId: Int,
    ): List<Int> {
        return dbQuery {
            FavoritePollTable.select {
                FavoritePollTable.userId eq userId
            }.map {
                it[FavoritePollTable.pollId]
            }
        }
    }
}