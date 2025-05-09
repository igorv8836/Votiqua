package com.example.feature.voting.ui.manage_poll_screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

actual fun copyToClipboard(text: String, context: Any?) {
    val ctx = context as? Context ?: return
    val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("copied_text", text)
    clipboard.setPrimaryClip(clip)
}
