package com.monsalud.bookshelf.data.local

import com.monsalud.bookshelf.data.LocalDataSource
import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookListDao
import com.monsalud.bookshelf.data.local.room.BookReviewDao
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber

class LocalDataSourceImpl(
    private val bookListDao: BookListDao,
    private val bookReviewDAO: BookReviewDao,
    private val dataStore: BookshelfDataStore,
) : LocalDataSource {

    /** Book List */

    override suspend fun getListWithBooks(listName: String): Flow<ListWithBooks?> {
        return bookListDao.getListWithBooks(listName)
            .catch { e ->
                Timber.e("Error getting list with books: ${e.message}")
                emit(null)
            }
    }

    override suspend fun insertListWithBooks(listWithBooks: ListWithBooks) {
        bookListDao.insertListWithBooks(listWithBooks)
    }

    override suspend fun deleteListWithBooks(listName: String) {
        bookListDao.deleteListWithBooks(listName)
    }


    /** Book Review */

    override suspend fun insertBookReview(bookReview: BookReviewEntity) {
        val reviewWithTimestamp = bookReview.copy(timestamp = System.currentTimeMillis())
        bookReviewDAO.insertBookReview(reviewWithTimestamp)
    }

    override suspend fun deleteBookReview(isbn13: String) {
        bookReviewDAO.deleteBookReview(isbn13)
    }

    override fun getBookReview(isbn13: String): Flow<BookReviewEntity?> {
        return bookReviewDAO.getBookReview(isbn13)
            .catch {
                Timber.e("Error getting book review: ${it.message}")
                emit(null)
            }
    }


    /** DataStore */

    override suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences> {
        return dataStore.preferencesFlow
    }

    override suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        dataStore.updateHasSeenOnboardingDialog(hasSeen)
    }
}
