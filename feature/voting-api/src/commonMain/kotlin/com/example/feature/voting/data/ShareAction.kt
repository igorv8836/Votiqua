package com.example.feature.voting.data

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module

fun shareCard(shareText: String, shareLink: String) {
    val service = ServiceLocator.shareService
    service.shareText(text = shareText, link = shareLink)
}

interface ShareService {
    fun shareText(text: String, link: String)
}

expect fun platformModule(): Module

internal object ServiceLocator : KoinComponent {
    val shareService: ShareService by inject()
}