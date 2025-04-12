package org.example.votiqua.server.feature.profile.data

import org.example.votiqua.models.profile.UserProfile
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.example.votiqua.server.feature.voting.database.PollAuthorTable
import org.example.votiqua.server.feature.voting.database.VoteTable
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class ProfileRepository {

    private fun getUserProfileWithoutDbBlocking(userId: Int): UserProfile? {
        val userRow = UserTable.select { UserTable.id eq userId }.singleOrNull() ?: return null

        val pollsCreated = PollAuthorTable
            .select { PollAuthorTable.authorId eq userId }
            .count()

        val pollsVoted = VoteTable
            .select { VoteTable.userId eq userId }
            .count()

        return UserProfile(
            id = userRow[UserTable.id],
            username = userRow[UserTable.username],
            email = userRow[UserTable.email],
            photoUrl = userRow[UserTable.photoUrl],
            description = userRow[UserTable.description],
            pollsCreated = pollsCreated.toInt(),
            pollsVoted = pollsVoted.toInt()
        )
    }

    suspend fun getUserProfile(userId: Int): UserProfile? {
        return dbQuery {
            getUserProfileWithoutDbBlocking(userId)
        }
    }

    suspend fun updateUserProfile(
        userId: Int,
        username: String? = null,
        description: String? = null,
        photoUrl: String? = null
    ): UserProfile? {
        return dbQuery {
            val userRow = UserTable.select { UserTable.id eq userId }.singleOrNull() ?: return@dbQuery null

            UserTable.update({ UserTable.id eq userId }) {
                username?.let { un -> it[UserTable.username] = un }
                description?.let { desc -> it[UserTable.description] = desc }
                photoUrl?.let { url -> it[UserTable.photoUrl] = url }
            }

            getUserProfileWithoutDbBlocking(userId)
        }
    }

    suspend fun searchUsers(query: String, limit: Int = 10): List<UserProfile> {
        return dbQuery {
            UserTable.select {
                (UserTable.username like "%$query%") or
                        (UserTable.email like "%$query%")
            }
                .limit(limit)
                .map { row ->
                    UserProfile(
                        id = row[UserTable.id],
                        username = row[UserTable.username],
                        email = row[UserTable.email],
                        photoUrl = row[UserTable.photoUrl],
                        description = row[UserTable.description]
                    )
                }
        }
    }
} 