package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.models.poll.Tag
import org.example.votiqua.server.feature.voting.database.PollTagTable
import org.example.votiqua.server.feature.voting.database.TagTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class TagRepository {

    fun insertTags(tags: List<Tag>, pollId: Int) {
        tags.forEach { tag ->
            val tagId = TagTable.select { TagTable.name eq tag.name }.singleOrNull()
                ?.get(TagTable.id) ?: run {
                    TagTable.insert {
                        it[TagTable.name] = tag.name
                    } get TagTable.id
                }

            PollTagTable.insert {
                it[PollTagTable.pollId] = pollId
                it[PollTagTable.tagId] = tagId
            }
        }
    }

    fun updatePollTags(poll: Poll) {
        PollTagTable.deleteWhere { PollTagTable.pollId eq poll.id }

        poll.tags.forEach { tag ->
            val tagId = TagTable.select { TagTable.name eq tag.name }
                .singleOrNull()?.get(TagTable.id) ?: (TagTable.insert {
                it[TagTable.name] = tag.name
            } get TagTable.id)

            PollTagTable.insert {
                it[PollTagTable.pollId] = poll.id
                it[PollTagTable.tagId] = tagId
            }
        }
    }

    fun getPollTags(pollId: Int): List<Tag> {
        val tags = (PollTagTable innerJoin TagTable)
            .select { PollTagTable.pollId eq pollId }
            .map { mapRowToTag(it) }

        return tags
    }

    private fun mapRowToTag(row: ResultRow): Tag {
        return Tag(
            id = row[TagTable.id],
            name = row[TagTable.name]
        )
    }
}