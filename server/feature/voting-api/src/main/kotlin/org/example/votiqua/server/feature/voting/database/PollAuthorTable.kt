package org.example.votiqua.server.feature.voting.database

import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PollAuthorTable : Table() {
    val pollId = integer("poll_id").references(PollTable.id, onDelete = ReferenceOption.CASCADE)
    val authorId = integer("author_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(pollId, authorId)
} 