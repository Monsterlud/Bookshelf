package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    /** Book List Operations */

    /**
     * Gets the book list with associated books from the local database.
     * @param listName The name of the book list to retrieve.
     * @return A Flow emitting the [ListWithBooks] object if found, or null if not found.
     */
    suspend fun getListWithBooks(listName: String): Flow<ListWithBooks?>

    /**
     * Inserts a new book list with associated books into the local database.
     * @param listWithBooks The [ListWithBooks] object to insert.
     */
    suspend fun insertListWithBooks(listWithBooks: ListWithBooks)

    /**
     * Deletes the book list with the given name from the local database.
     * @param listName The name of the book list to delete.
     */
    suspend fun deleteListWithBooks(listName: String)


    /** Book Review Operations */

    /**
     * Inserts a new book review into the local database.
     * @param bookReview The [BookReviewEntity] object to insert.
     */
    suspend fun insertBookReview(bookReview: BookReviewEntity)

    /**
     * Deletes the book review with the given ISBN from the local database.
     * @param isbn13 The ISBN of the book review to delete.
     */
    suspend fun deleteBookReview(isbn13: String)

    /**
     * Gets the book review with the given ISBN from the local database.
     * @param isbn13 The ISBN of the book review to retrieve.
     * @return A Flow emitting the [BookReviewEntity] if found, or null if not found.
     */
    fun getBookReview(isbn13: String): Flow<BookReviewEntity?>


    /** DataStore Operations */

    /**
     * Gets the user preferences Flow from the DataStore.
     * @return A Flow emitting the [BookshelfDataStore.UserPreferences] object.
     */
    suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences>

    /**
     * Updates the "has seen onboarding dialog" preference in the DataStore.
     * @param hasSeen True if the user has seen the onboarding dialog, false otherwise.
     */
    suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean)
}
