package com.example.feature.voting.utils

import kotlinx.datetime.LocalDateTime

fun formatDate(dateTime: LocalDateTime): String = buildString {
    append(dateTime.dayOfMonth.toString().padStart(2, '0'))
    append('-')
    append(dateTime.monthNumber.toString().padStart(2, '0'))
    append('-')
    append(dateTime.year.toString().padStart(4, '0'))
}

fun formatTime(dateTime: LocalDateTime): String = buildString {
    append(dateTime.hour.toString().padStart(2, '0'))
    append(':')
    append(dateTime.minute.toString().padStart(2, '0'))
}