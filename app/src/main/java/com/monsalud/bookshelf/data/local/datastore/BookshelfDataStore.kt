package com.monsalud.bookshelf.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val BOOKSHELF_PREFERENCES = "bookshelf_preferences"
val Context.bookshelfPreferencesDatastore: DataStore<Preferences> by preferencesDataStore(
    name = BOOKSHELF_PREFERENCES
)

class BookshelfDataStore(
    private val context: Context
) {
    private val dataStore = context.bookshelfPreferencesDatastore

    internal object PreferencesKeys {
        val HAS_SEEN_ONBOARDING_DIALOG = booleanPreferencesKey("has_seen_onboarding_dialog")
    }
    data class UserPreferences(
        val hasSeenOnboardingDialog: Boolean
    )

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val hasSeenOnboardingDialog =
                preferences[PreferencesKeys.HAS_SEEN_ONBOARDING_DIALOG] ?: false
            UserPreferences(hasSeenOnboardingDialog = hasSeenOnboardingDialog)

        }

    suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_SEEN_ONBOARDING_DIALOG] = hasSeen
        }
    }
}
