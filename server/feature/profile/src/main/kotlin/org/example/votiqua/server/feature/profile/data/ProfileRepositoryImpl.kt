package org.example.votiqua.server.feature.profile.data

import org.example.votiqua.models.profile.UserProfile
import org.example.votiqua.server.common.models.MainConfig
import org.example.votiqua.server.common.models.OutOfConfigRangeException
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.example.votiqua.server.feature.voting.database.PollTable
import org.example.votiqua.server.feature.voting.database.VoteTable
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class ProfileRepositoryImpl(
    private val profilePhotoUrlConverter: ProfilePhotoUrlConverter,
) : ProfileRepository {

    private fun getUserProfileWithoutDbBlocking(userId: Int): UserProfile? {
        val userRow = UserTable.select { UserTable.id eq userId }.singleOrNull() ?: return null

        val pollsCreated = PollTable
            .select { PollTable.authorId eq userId }
            .count()

        val pollsVoted = VoteTable
            .select { VoteTable.userId eq userId }
            .count()

        val photoUrl = if (userRow[UserTable.photoUrl] != null)
            profilePhotoUrlConverter.getUserPhotoUrl(
                imageName = userRow[UserTable.photoUrl],
                userRow[UserTable.id],
            )
        else
            null

        return UserProfile(
            id = userRow[UserTable.id],
            username = userRow[UserTable.username],
            email = userRow[UserTable.email],
            photoUrl = photoUrl,
            originalPhotoUrl = userRow[UserTable.photoUrl],
            description = userRow[UserTable.description],
            pollsCreated = pollsCreated.toInt(),
            pollsVoted = pollsVoted.toInt()
        )
    }

    override suspend fun getUserProfile(userId: Int): UserProfile? {
        return dbQuery {
            getUserProfileWithoutDbBlocking(userId)
        }
    }

    override suspend fun updateUserProfile(
        userId: Int,
        username: String?,
        description: String?,
        photoUrl: String?,
    ): UserProfile? {
        username?.let {
            if (MainConfig.MIN_NICKNAME_LEN.value > username.length) {
                throw OutOfConfigRangeException(MainConfig.MIN_NICKNAME_LEN.text)
            }
            if (MainConfig.MAX_NICKNAME_LEN.value < username.length) {
                throw OutOfConfigRangeException(MainConfig.MAX_NICKNAME_LEN.text)
            }
        }

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

    override suspend fun searchUsers(query: String, limit: Int): List<UserProfile> {
        return dbQuery {
            UserTable.select {
                (UserTable.username like "%$query%") or
                        (UserTable.email like "%$query%")
            }
                .limit(limit)
                .map { row ->

                    val photoUrl = if (row[UserTable.photoUrl] != null)
                        profilePhotoUrlConverter.getUserPhotoUrl(
                            imageName = row[UserTable.photoUrl],
                            row[UserTable.id]
                        )
                    else
                        null

                    UserProfile(
                        id = row[UserTable.id],
                        username = row[UserTable.username],
                        email = row[UserTable.email],
                        photoUrl = photoUrl,
                        originalPhotoUrl = row[UserTable.photoUrl],
                        description = row[UserTable.description]
                    )
                }
        }
    }
} 