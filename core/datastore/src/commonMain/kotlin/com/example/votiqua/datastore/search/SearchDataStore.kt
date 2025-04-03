package com.example.votiqua.datastore.search

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchDataStore(private val dataStore: DataStore<Preferences>) {
    val searchQueries: Flow<List<String>> = dataStore.data.map { prefs ->
        prefs[SearchKeys.SEARCH_QUERIES]?.reversed() ?: emptyList()
    }

    suspend fun addQuery(query: String) {
        dataStore.edit { prefs ->
            val currentQueries = prefs[SearchKeys.SEARCH_QUERIES]?.toMutableSet() ?: mutableSetOf()

            currentQueries.remove(query)
            currentQueries.add(query)

            if (currentQueries.size > QUERY_COUNT) {
                val iterator = currentQueries.iterator()
                if (iterator.hasNext()) {
                    iterator.next()
                    iterator.remove()
                }
            }

            prefs[SearchKeys.SEARCH_QUERIES] = currentQueries
        }
    }

    suspend fun clearQuery(query: String) {
        dataStore.edit { prefs ->
            val currentQueries = prefs[SearchKeys.SEARCH_QUERIES]?.toMutableSet() ?: mutableSetOf()
            currentQueries.remove(query)
            prefs[SearchKeys.SEARCH_QUERIES] = currentQueries
        }
    }

    companion object {
        private const val QUERY_COUNT = 5
    }
}

object SearchKeys {
    val SEARCH_QUERIES = stringSetPreferencesKey("search_queries")
}