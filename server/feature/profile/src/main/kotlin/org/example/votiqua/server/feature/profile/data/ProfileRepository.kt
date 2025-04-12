package org.example.votiqua.server.feature.profile.data

import org.example.votiqua.models.profile.UserProfile
import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.example.votiqua.server.feature.voting.database.PollAuthorTable
import org.example.votiqua.server.feature.voting.database.VoteTable
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object ProfileRepository {
    
    fun getUserProfile(userId: Int): UserProfile? {
        return transaction {
            val userRow = UserTable.select { UserTable.id eq userId }.singleOrNull() ?: return@transaction null
            
            val pollsCreated = PollAuthorTable
                .select { PollAuthorTable.authorId eq userId }
                .count()
            
            val pollsVoted = VoteTable
                .select { VoteTable.userId eq userId }
                .count()
            
            UserProfile(
                id = userRow[UserTable.id],
                username = userRow[UserTable.username],
                email = userRow[UserTable.email],
//                photoUrl = userRow[UserTable.photoUrl], //TODO
//                bio = userRow[UserTable.bio],
                pollsCreated = pollsCreated.toInt(),
                pollsVoted = pollsVoted.toInt()
            )
        }
    }
    
    fun updateUserProfile(userId: Int, username: String? = null, bio: String? = null, photoUrl: String? = null): UserProfile? {
        return transaction {
            val userRow = UserTable.select { UserTable.id eq userId }.singleOrNull() ?: return@transaction null
            
            UserTable.update({ UserTable.id eq userId }) {
                username?.let { un -> it[UserTable.username] = un }
//                bio?.let { b -> it[UserTable.bio] = b } //TODO
//                photoUrl?.let { url -> it[UserTable.photoUrl] = url }
            }
            
            getUserProfile(userId)
        }
    }
    
    fun searchUsers(query: String, limit: Int = 10): List<UserProfile> {
        return transaction {
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
//                    photoUrl = row[UserTable.photoUrl], //TODO
//                    bio = row[UserTable.bio]
                )
            }
        }
    }
} 