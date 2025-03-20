package org.example.votiqua.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val photo = binary("photo").nullable()
    val notificationEnabled = bool("notification_enabled")
    val isActive = bool("is_active")
    val banReason = varchar("ban_reason", 255).nullable()
    val passwordHash = varchar("password_hash", 255).nullable()
    val createdAt = datetime("created_at")
    val isAdmin = bool("is_admin").default(false)
    override val primaryKey = PrimaryKey(id)
}