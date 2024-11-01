package com.monsalud.bookshelf.domain

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import kotlinx.coroutines.flow.Flow
import kotlin.Result

interface BookshelfRepository {

    /** Book List Operations */

    /**
     * Refreshes the book list in the local database by fetching data from the API.
     * @param listName The name of the book list to refresh.
     */
    suspend fun refreshBookListInDbFromApi(listName: String) : Result<Unit>

    /**
     * Gets the book list with associated books from the local database.
     * @param listName The name of the book list to retrieve.
     * @return A Flow emitting the [ListWithBooks] object if found, or null if not found.
     */
    suspend fun getListWithBooks(listName: String) : Flow<ListWithBooks?>

    /**
     * Checks if data exists in the local database for the given book list.
     * @param listName The name of the book list to check.
     * @return True if data exists, false otherwise.
     */
    suspend fun hasDataForList(listName: String) : Boolean


    /**
     * Book Review Operations
     */

    /**
     * Gets the book review for the given ISBN.
     * This method first checks the local database for a cached review. If a cached review is found
     * and is not older than a certain threshold, it is returned. Otherwise, it fetches the review
     * from the API, saves it to the database, and then returns it.
     * @param isbn The ISBN of the book to get the review for.
     * @return A Flow emitting a [Result] object containing the [BookReviewEntity] if successful,
     * or an error if something went wrong.
     */
    suspend fun getBookReview(isbn: String) : Flow<Result<BookReviewEntity?>>


    /**
     * User Preferences Operations
     */

    /**
     * Gets the user preferences Flow from the DataStore.
     * @return A Flow emitting the [BookshelfDataStore.UserPreferences] object.
     */
    suspend fun getUserPreferencesFlow() : Flow<BookshelfDataStore.UserPreferences>

    /**
     * Updates the "has seen onboarding dialog" preference in the DataStore.
     * @param hasSeen True if the user has seen the onboarding dialog, false otherwise.
     */
    suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean)
}
