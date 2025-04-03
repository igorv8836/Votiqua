package com.example.votiqua.datastore

import com.example.votiqua.datastore.search.SearchDataStore
import com.example.votiqua.datastore.settings.SettingsDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

fun datastoreModule() = module {
    includes(createDataStoreModule())

    single { SettingsDataStore(get()) }
    single { SearchDataStore(get()) }
}


expect fun createDataStoreModule(): Module