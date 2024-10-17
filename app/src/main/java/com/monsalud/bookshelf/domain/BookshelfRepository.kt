package com.monsalud.bookshelf.domain

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import kotlinx.coroutines.flow.Flow

interface BookshelfRepository {

    /** Book List */

    suspend fun refreshBookListInDB(listName: String)

    suspend fun getListWithBooks(listName: String) : Flow<ListWithBooks?>


    /** Book Review */

    suspend fun getBookReviewFromApiAndSaveInDB(isbn: String) : Flow<BookReviewEntity?>

    suspend fun getBookReview(isbn: String) : Flow<BookReviewEntity?>


    /** User Preferences */

    suspend fun getUserPreferencesFlow() : Flow<BookshelfDataStore.UserPreferences>

    suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean)
}
