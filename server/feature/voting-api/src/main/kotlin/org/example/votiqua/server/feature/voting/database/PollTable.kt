package org.example.votiqua.server.feature.voting.database

import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object PollTable : Table() {
    val id = integer("id").autoIncrement()
    val question = text("question")
    val description = text("description").nullable()
    val isAnonymous = bool("is_anonymous").default(false)
    val isOpen = bool("is_open").default(false)
    val createdAt = datetime("created_at")
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
    val authorId = integer("author_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val isStarted = bool("is_started").default(false)
    val link = text("link").nullable().uniqueIndex()
    override val primaryKey = PrimaryKey(id)
}