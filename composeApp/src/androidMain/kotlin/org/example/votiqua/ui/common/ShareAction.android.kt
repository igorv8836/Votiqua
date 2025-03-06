package org.example.votiqua.ui.common

import android.content.Context
import android.content.Intent
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidShareService(private val context: Context) : ShareService {
    override fun shareText(text: String, link: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            putExtra(Intent.EXTRA_TITLE, text)
            type = "text/plain"
        }

        context.startActivity(
            Intent.createChooser(shareIntent, null).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}

actual fun platformModule(): Module = module {
    single<ShareService> { AndroidShareService(get()) }
}