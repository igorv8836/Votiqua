package org.example.votiqua.data.repository

import com.example.common.ThemeMode
import com.example.votiqua.datastore.settings.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import org.example.votiqua.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val settingsDataStore: SettingsDataStore,
) : ProfileRepository {
    override fun getThemeMode(): Flow<ThemeMode> {
        return settingsDataStore.appTheme
    }

    override suspend fun setThemeMode(mode: ThemeMode): Result<Unit> {
        return runCatching {
            settingsDataStore.setAppTheme(mode)
        }
    }
}