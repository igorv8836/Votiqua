package org.example.votiqua.server.feature.voting.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object PollTable : Table() {
    val id = integer("id").autoIncrement()
    val question = text("question")
    val description = text("description").nullable()
    val isMultiple = bool("is_multiple").default(false)
    val isAnonymous = bool("is_anonymous").default(false)
    val isOpen = bool("is_open").default(false)
    val createdAt = datetime("created_at")
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
    override val primaryKey = PrimaryKey(id)
}