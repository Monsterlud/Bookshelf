package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    /** Book List */

    suspend fun getListWithBooks(listName: String): Flow<ListWithBooks?>

    fun getBooksForList(listName: String): Flow<List<BookEntity>>

    suspend fun insertListWithBooks(listWithBooks: ListWithBooks)

    suspend fun deleteListWithBooks(listName: String)

    suspend fun insertBooks(books: List<BookEntity>)

    suspend fun deleteBooksForList(isbn13: String)

    fun getAllBooks(): Flow<List<BookEntity>>


    /** Book Review */

    suspend fun insertBookReview(bookReview: BookReviewEntity)

    suspend fun deleteBookReview(isbn13: String)

    fun getBookReview(isbn13: String): Flow<BookReviewEntity?>

    /** DataStore */

    suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences>

    suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean)
}
