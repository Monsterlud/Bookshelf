package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    /** Book List Operations */

    /**
     * Retrieves a list with its associated books by list name.
     * @param listName The name of the list to retrieve.
     * @return A Flow emitting the ListWithBooks object or null if not found.
     */
    suspend fun getListWithBooks(listName: String): Flow<ListWithBooks?>

    /**
     * Inserts a list with its associated books into the local database.
     * @param listWithBooks The ListWithBooks object to insert.
     */
    suspend fun insertListWithBooks(listWithBooks: ListWithBooks)

    /**
     * Deletes a list and its associated books from the local database.
     * @param listName The name of the list to delete.
     */
    suspend fun deleteListWithBooks(listName: String)


    /** Book Review Operations */

    /**
     * Inserts a book review into the local database.
     * @param bookReview The BookReviewEntity to insert.
     */
    suspend fun insertBookReview(bookReview: BookReviewEntity)

    /**
     * Deletes a book review from the local database.
     * @param isbn13 The ISBN13 of the book review to delete.
     */
    suspend fun deleteBookReview(isbn13: String)

    /**
     * Retrieves a book review for a specific book.
     * @param isbn13 The ISBN13 of the book to get the review for.
     * @return A Flow emitting the BookReviewEntity or null if not found.
     */
    fun getBookReview(isbn13: String): Flow<BookReviewEntity?>

    /** DataStore Operations */

    /**
     * Retrieves the user preferences as a Flow.
     * @return A Flow emitting the UserPreferences object.
     */
    suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences>

    /**
     * Updates the user's onboarding dialog seen status.
     * @param hasSeen Boolean indicating whether the user has seen the onboarding dialog.
     */
    suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean)
}
