package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import com.monsalud.bookshelf.domain.BookshelfRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import java.io.IOException

class FakeRepository : BookshelfRepository{
    private var bookReviewResult: Result<BookReviewEntity?> = Result.success(null)
    private var getListWithBooksResult: ListWithBooks? = null
    private var hasDataForListResult: Boolean = false
    var updateHasSeenOnboardingDialogCalled = false
    var lastHasSeenOnboardingDialogValue: Boolean? = null
    private val preferencesFlow = MutableStateFlow(
        BookshelfDataStore.UserPreferences(hasSeenOnboardingDialog = false)
    )
    private var shouldSimulateNetworkError = false

    override suspend fun refreshBookListInDbFromApi(listName: String): Result<Unit> {
        return if (shouldSimulateNetworkError) {
            Result.failure(IOException("Network error"))
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun getListWithBooks(listName: String): Flow<ListWithBooks?> {
        Timber.d("getListWithBooks called with listName: $listName, returning $getListWithBooksResult")
        return flowOf(getListWithBooksResult)
    }

    override suspend fun hasDataForList(listName: String): Boolean {
        return hasDataForListResult
    }

    override suspend fun getBookReview(isbn: String): Flow<Result<BookReviewEntity?>> {
        return flowOf(bookReviewResult)
    }

    override suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences> {
        return preferencesFlow
    }

    override suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        updateHasSeenOnboardingDialogCalled = true
        lastHasSeenOnboardingDialogValue = hasSeen
        preferencesFlow.value = BookshelfDataStore.UserPreferences(hasSeenOnboardingDialog = hasSeen)
    }


    // These are helper methods to tweak the flow returned by getBookReview

    fun setBookReviewSuccess(review: BookReviewEntity) {
        bookReviewResult = Result.success(review)
    }
    fun setBookReviewFailure(exception: Exception) {
        bookReviewResult = Result.failure(exception)
    }
    fun setBookReviewEmpty() {
        bookReviewResult = Result.success(null)
    }

    fun setGetListWithBooksResult(listWithBooks: ListWithBooks?) {
        getListWithBooksResult = listWithBooks
    }

    fun setHasDataForListResult(hasData: Boolean) {
        hasDataForListResult = hasData
    }

    fun setUserPreferences(preferences: BookshelfDataStore.UserPreferences) {
        preferencesFlow.value = preferences
    }

    fun setShouldSimulateNetworkError(shouldSimulate: Boolean) {
        shouldSimulateNetworkError = shouldSimulate
    }
}