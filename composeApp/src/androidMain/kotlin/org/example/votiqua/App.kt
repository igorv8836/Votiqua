package org.example.votiqua

import android.app.Application
import org.example.votiqua.di.KoinFactory
import org.koin.android.ext.koin.androidContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinFactory.setupKoin {
            androidContext(this@App)
        }
    }
}