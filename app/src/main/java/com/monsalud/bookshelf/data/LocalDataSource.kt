package com.monsalud.bookshelf.data

interface LocalDataSource {
    /** DataStore */

    suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences>
}