package org.example.votiqua.server.feature.auth.data

import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.common.utils.toUtcTimestamp
import org.example.votiqua.server.feature.auth.api.database.PasswordResetTable
import org.example.votiqua.server.feature.auth.domain.models.PasswordResetModel
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class PasswordResetRepository {
    suspend fun saveResetCode(email: String): Int {
        return dbQuery {
            val resetCode = (100000..999999).random()

            val existingRecord = PasswordResetTable.select {
                PasswordResetTable.email.eq(email)
            }.firstOrNull()

            if (existingRecord == null) {
                PasswordResetTable.insert {
                    it[PasswordResetTable.email] = email
                    it[code] = resetCode
                    it[createdAt] = currentDateTime()
                }
            } else {
                PasswordResetTable.update({ PasswordResetTable.email.eq(email) }) {
                    it[code] = resetCode
                    it[createdAt] = currentDateTime()
                    it[countInputAttempts] = 0
                }
            }
            resetCode
        }
    }

    suspend fun getResetRecord(email: String): PasswordResetModel? {
        return dbQuery {
            PasswordResetTable.select {
                PasswordResetTable.email.eq(email)
            }.firstOrNull()?.let {
                PasswordResetModel(
                    email = it[PasswordResetTable.email],
                    code = it[PasswordResetTable.code],
                    createdAt = it[PasswordResetTable.createdAt].toUtcTimestamp(),
                    countInputAttempts = it[PasswordResetTable.countInputAttempts],
                    isUsed = it[PasswordResetTable.isUsed]
                )
            }
        }
    }

    suspend fun incrementInputAttempts(email: String) {
        dbQuery {
            PasswordResetTable.update({ PasswordResetTable.email.eq(email) }) {
                with(SqlExpressionBuilder) {
                    it.update(countInputAttempts, countInputAttempts + 1)
                }
            }
        }
    }

    suspend fun markAsUsed(email: String) {
        dbQuery {
            PasswordResetTable.update({ PasswordResetTable.email.eq(email) }) {
                it[isUsed] = true
            }
        }
    }
}
