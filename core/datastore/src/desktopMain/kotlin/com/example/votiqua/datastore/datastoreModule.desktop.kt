package com.example.votiqua.datastore

import org.koin.dsl.module

actual fun createDataStoreModule() = module {
    single { createDataStore { dataStoreFileName } }
}