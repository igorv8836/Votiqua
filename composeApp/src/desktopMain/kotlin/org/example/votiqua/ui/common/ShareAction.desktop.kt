package org.example.votiqua.ui.common

import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidShareService() : ShareService {
    override fun shareText(text: String, link: String) {

    }
}

actual fun platformModule(): Module = module {
    single<ShareService> { AndroidShareService() }
}