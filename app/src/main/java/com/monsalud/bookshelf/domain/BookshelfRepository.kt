package com.monsalud.bookshelf.domain

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import kotlinx.coroutines.flow.Flow

interface BookshelfRepository {

    /** Book List Operations */

    /**
     * Refreshes a specific book list in the local database with data from the remote API.
     *
     * @param listName The name of the list to refresh.
     */
    suspend fun refreshBookListInDB(listName: String)

    /**
     * Retrieves a list with its associated books, either from the local database or remote API.
     *
     * @param listName The name of the list to retrieve.
     * @return A Flow emitting the ListWithBooks object or null if not found.
     */
    suspend fun getListWithBooks(listName: String) : Flow<ListWithBooks?>


    /** Book Review Operations */

    /**
     * Fetches a book review from the remote API and saves it in the local database.
     *
     * @param isbn The ISBN of the book to get the review for.
     * @return A Flow emitting the BookReviewEntity or null if no review is found.
     */
    suspend fun getBookReviewFromApiAndSaveInDB(isbn: String) : Flow<BookReviewEntity?>

    /**
     * Retrieves a book review, prioritizing the local database but potentially fetching from the API if not found locally.
     *
     * @param isbn The ISBN of the book to get the review for.
     * @return A Flow emitting the BookReviewEntity or null if no review is found.
     */
    suspend fun getBookReview(isbn: String) : Flow<BookReviewEntity?>


    /** User Preferences Operations */

    /**
     * Retrieves the user preferences as a Flow.
     *
     * @return A Flow emitting the UserPreferences object.
     */
    suspend fun getUserPreferencesFlow() : Flow<BookshelfDataStore.UserPreferences>

    /**
     * Updates the user's onboarding dialog seen status.
     *
     * @param hasSeen Boolean indicating whether the user has seen the onboarding dialog.
     */
    suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean)
}
