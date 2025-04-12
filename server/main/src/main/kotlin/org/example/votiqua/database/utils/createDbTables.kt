package org.example.votiqua.database.utils

import org.example.votiqua.database.tables.FavoritePollTable
import org.example.votiqua.database.tables.NotificationTable
import org.example.votiqua.database.tables.RecommendedFeedTable
import org.example.votiqua.database.tables.SearchHistoryTable
import org.example.votiqua.server.feature.auth.api.database.PasswordResetTable
import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.example.votiqua.server.feature.voting.database.PollAuthorTable
import org.example.votiqua.server.feature.voting.database.PollOptionTable
import org.example.votiqua.server.feature.voting.database.PollParticipantTable
import org.example.votiqua.server.feature.voting.database.PollTable
import org.example.votiqua.server.feature.voting.database.PollTagTable
import org.example.votiqua.server.feature.voting.database.TagTable
import org.example.votiqua.server.feature.voting.database.VoteTable
import org.jetbrains.exposed.sql.SchemaUtils


fun createDbTables() {
    SchemaUtils.create(
        PollOptionTable,
        PollTable,
        UserTable,
        VoteTable,
        PasswordResetTable,
        TagTable,
        PollTagTable,
        PollAuthorTable,
        NotificationTable,
        FavoritePollTable,
        RecommendedFeedTable,
        SearchHistoryTable,
        PollParticipantTable
    )
}