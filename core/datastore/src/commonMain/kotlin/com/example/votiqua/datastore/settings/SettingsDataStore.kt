package com.example.votiqua.datastore.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.common.ThemeMode
import com.example.common.toThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val dataStore: DataStore<Preferences>) {
    val appTheme: Flow<ThemeMode> = dataStore.data.map { prefs ->
        (prefs[SettingsKeys.APP_THEME] ?: DEFAULT_APP_THEME).toThemeMode()
    }

    suspend fun setAppTheme(type: ThemeMode) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.APP_THEME] = type.value
        }
    }

    companion object {
        // 1 - Light, 2 - Dark, 3 - System
        private const val DEFAULT_APP_THEME = 3
    }
}

object SettingsKeys {
    val APP_THEME = intPreferencesKey("app_theme")
}