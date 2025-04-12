package org.example.votiqua.server.feature.voting.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PollOptionTable : Table() {
    val id = integer("id").autoIncrement()
    val pollId = integer("poll_id").references(PollTable.id, onDelete = ReferenceOption.CASCADE)
    val optionText = text("option_text")
    val orderIndex = integer("order_index").default(0)
    override val primaryKey = PrimaryKey(id)
}