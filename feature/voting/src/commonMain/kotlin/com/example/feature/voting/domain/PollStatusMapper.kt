package com.example.feature.voting.domain

import kotlinx.datetime.Clock

class PollStatusMapper {
    fun map(
        isStarted: Boolean,
        startTime: Long?,
        endTime: Long?,
    ): String {
        val now = Clock.System.now().epochSeconds
        return when {
            !isStarted -> "Не запущено"
            endTime != null && endTime < now -> "Завершено"
            startTime != null && endTime != null -> {
                if (startTime < now && endTime > now) {
                    "Активно"
                } else {
                    "Ожидание"
                }
            }
            else -> {
                "Не запущено"
            }
        }
    }
}