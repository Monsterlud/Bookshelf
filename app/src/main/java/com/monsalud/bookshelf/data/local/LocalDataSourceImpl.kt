package com.monsalud.bookshelf.data.local

import com.monsalud.bookshelf.data.LocalDataSource

class LocalDataSourceImpl() : LocalDataSource {
    private val dataStore: BookshelfDataStore,
    /** DataStore */
    override suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences> {
        return dataStore.preferencesFlow
    }
}