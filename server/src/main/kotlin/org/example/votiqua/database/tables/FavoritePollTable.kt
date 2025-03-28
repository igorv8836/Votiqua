package org.example.votiqua.database.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object FavoritePollTable : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val pollId = integer("poll_id").references(PollTable.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}