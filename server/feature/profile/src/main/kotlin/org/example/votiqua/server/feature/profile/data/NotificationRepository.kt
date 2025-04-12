package org.example.votiqua.server.feature.profile.data

import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.feature.profile.database.NotificationTable
import org.jetbrains.exposed.sql.insert

class NotificationRepository {
    
    suspend fun createNotification(userId: Int, message: String, type: String): Int {
        return dbQuery {
            NotificationTable.insert {
                it[NotificationTable.userId] = userId
                it[NotificationTable.message] = message
                it[NotificationTable.type] = type
                it[isRead] = false
                it[createdAt] = currentDateTime()
            } get NotificationTable.id
        }
    }

    suspend fun createPollNotification(userId: Int, pollId: Int, creatorName: String): Int {
        return createNotification(
            userId = userId,
            message = "Пользователь $creatorName создал новое голосование, которое может вас заинтересовать",
            type = "NEW_POLL"
        )
    }

    suspend fun createVoteNotification(authorId: Int, voterName: String, pollQuestion: String): Int {
        return createNotification(
            userId = authorId,
            message = "Пользователь $voterName проголосовал в вашем опросе \"$pollQuestion\"",
            type = "NEW_VOTE"
        )
    }

    suspend fun createFavoriteNotification(authorId: Int, userName: String, pollQuestion: String): Int {
        return createNotification(
            userId = authorId,
            message = "Пользователь $userName добавил ваш опрос \"$pollQuestion\" в избранное",
            type = "NEW_FAVORITE"
        )
    }
}