package com.example.feature.voting.data

import org.koin.dsl.module

actual fun platformModule() = module {
    single<ShareService> {
        ShareServiceImpl()
    }
}

class ShareServiceImpl : ShareService {
    override fun shareText(text: String, link: String) {

    }
}