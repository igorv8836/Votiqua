package org.example.votiqua.database.tables

import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object NotificationTable : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val message = text("message")
    val type = varchar("type", 50)
    val isRead = bool("is_read").default(false)
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}