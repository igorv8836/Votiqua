package org.example.votiqua.server.feature.auth.api.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object PasswordResetTable : Table() {
    val id = integer("id").autoIncrement()
    val email = reference("email", UserTable.email, ReferenceOption.CASCADE)
    val code = integer("code")
    val countInputAttempts = integer("count_input_attempts").default(0)
    val createdAt = datetime("created_at")
    val isUsed = bool("is_used").default(false)
    override val primaryKey = PrimaryKey(id)
}