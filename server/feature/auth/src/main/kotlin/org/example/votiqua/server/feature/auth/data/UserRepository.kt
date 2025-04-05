package org.example.votiqua.server.feature.auth.data

import org.example.votiqua.models.common.ErrorType
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.auth.UserModel
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.common.utils.toTimestampMoscow
import org.example.votiqua.server.feature.auth.database.UserTable
import org.example.votiqua.server.feature.auth.utils.getPhotoPath
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class UserRepository {

    suspend fun insertUser(userModel: UserModel) {
        return dbQuery {
            val existingUsername = UserTable.select { UserTable.username eq userModel.username }.singleOrNull()
            val existingEmail = UserTable.select { UserTable.email eq userModel.email }.singleOrNull()
            if (existingEmail != null) {
                throw HTTPConflictException(ErrorType.USER_ALREADY_EXISTS.message)
            }
            if (existingUsername != null) {
                throw HTTPConflictException(ErrorType.USERNAME_ALREADY_EXISTS.message)
            }

            UserTable.insert { table ->
                table[email] = userModel.email
                table[username] = userModel.username
                table[passwordHash] = userModel.passwordHash
                table[notificationEnabled] = userModel.notificationEnabled
                table[isActive] = userModel.isActive
                table[banReason] = userModel.banReason
                table[createdAt] = LocalDateTime.now()
            }
        }
    }

    suspend fun getUserByEmail(email: String): UserModel? {
        return dbQuery {
            UserTable.select { UserTable.email eq email }
                .mapNotNull { toUserModel(it) }
                .singleOrNull()
        }
    }

    suspend fun isUsernameTaken(username: String): Boolean = dbQuery {
        UserTable.select { UserTable.username eq username }.singleOrNull() != null
    }

    suspend fun checkUser(email: String, passwordHash: String): UserModel? {
        return dbQuery {
            UserTable.select { (UserTable.email eq email) and (UserTable.passwordHash eq passwordHash) and (UserTable.passwordHash.isNotNull())}
                .mapNotNull { toUserModel(it) }
                .singleOrNull()
        }
    }

    suspend fun updatePassword(email: String, passwordHash: String) {
        return dbQuery {
            UserTable.update({ UserTable.email eq email }) {
                it[UserTable.passwordHash] = passwordHash
            }
        }
    }

    suspend fun deleteUser(email: String, passwordHash: String) {
        return dbQuery {
            UserTable.deleteWhere { (UserTable.email eq email) and (UserTable.passwordHash eq passwordHash) }
        }
    }

    private fun toUserModel(row: ResultRow): UserModel {
        return UserModel(
            id = row[UserTable.id],
            email = row[UserTable.email],
            passwordHash = row[UserTable.passwordHash],
            username = row[UserTable.username],
            photoUrl = if (row[UserTable.photo] != null) getPhotoPath(row[UserTable.id]) else null,
            notificationEnabled = row[UserTable.notificationEnabled],
            isActive = row[UserTable.isActive],
            banReason = row[UserTable.banReason],
            createdAt = row[UserTable.createdAt].toTimestampMoscow(),
            isAdmin = row[UserTable.isAdmin]
        )
    }
}
