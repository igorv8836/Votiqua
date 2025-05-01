package com.example.votiqua.datastore.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenDataStore(
    private val dataStore: DataStore<Preferences>
) {
    private val tokenKey = stringPreferencesKey("auth_token")

    val tokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[tokenKey]
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(tokenKey)
        }
    }
}