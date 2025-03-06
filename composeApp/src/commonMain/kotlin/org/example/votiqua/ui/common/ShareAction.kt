package org.example.votiqua.ui.common

import org.example.votiqua.di.KoinFactory
import org.koin.core.module.Module

fun shareCard(shareText: String, shareLink: String) {
    val service: ShareService = KoinFactory.getDI().get()
    service.shareText(text = shareText, link = shareLink)
}

interface ShareService {
    fun shareText(text: String, link: String)
}

expect fun platformModule(): Module