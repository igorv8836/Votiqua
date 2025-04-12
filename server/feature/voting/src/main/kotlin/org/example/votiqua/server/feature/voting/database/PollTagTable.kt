package org.example.votiqua.server.feature.voting.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PollTagTable : Table() {
    val pollId = integer("poll_id").references(PollTable.id, onDelete = ReferenceOption.CASCADE)
    val tagId = integer("tag_id").references(TagTable.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(pollId, tagId)
}