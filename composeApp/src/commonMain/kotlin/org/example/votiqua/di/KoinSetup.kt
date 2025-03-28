package org.example.votiqua.di

import com.example.common.commonModule
import com.example.votiqua.datastore.createDataStoreModule
import com.example.votiqua.datastore.datastoreModule
import com.example.votiqua.network.di.networkModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.example.votiqua.data.dataModule
import org.example.votiqua.ui.common.platformModule
import org.example.votiqua.ui.uiModule
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        Napier.base(DebugAntilog())
        appDeclaration()
        modules(
            commonModule(),
            dataModule(),
            uiModule(),
            platformModule(),
            networkModule(),
            createDataStoreModule(),
            datastoreModule(),
        )

    }

object KoinFactory {
    private var di: Koin? = null

    fun setupKoin(appDeclaration: KoinAppDeclaration = {}) {
        if (di == null) {
            di = initKoin(appDeclaration).koin
        }
    }

    fun getDI(): Koin {
        return di ?: run {
            setupKoin()
            di ?: throw IllegalStateException("Koin is not initialized")
        }
    }
}