package com.monsalud.bookshelf.data.local

import androidx.datastore.dataStore
import com.monsalud.bookshelf.data.LocalDataSource
import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookListDAO
import com.monsalud.bookshelf.data.local.room.BookReviewDAO
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class LocalDataSourceImpl(
    private val bookListDao: BookListDAO,
    private val bookReviewDAO: BookReviewDAO,
    private val dataStore: BookshelfDataStore,
) : LocalDataSource {

    /** Book List */

    override suspend fun getListWithBooks(listName: String): Flow<ListWithBooks> {
        //TODO: remove after debugging:
        Timber.d("localdatasource: listName: $listName")
        val listFlow = bookListDao.getListWithBooks("Hardcover Fiction")

        Timber.d("localdatasource: listflow: $listFlow")
        listFlow.collect {
            Timber.d("ListName: ${it.bookList.listName}")
            it.books.forEach { book ->
                it.books.forEach {
                    Timber.d("Book: ${it.title}")
                }
            }
        }
        return listFlow
    }





    override fun getBooksForList(listName: String): Flow<List<BookEntity>> {
        return bookListDao.getBooksForList(listName)
    }

    override suspend fun insertListWithBooks(listWithBooks: ListWithBooks) {
        bookListDao.insertListWithBooks(listWithBooks)
    }

    override suspend fun deleteListWithBooks(listName: String) {
        bookListDao.deleteListWithBooks(listName)
    }

    override suspend fun insertBooks(books: List<BookEntity>) {
        bookListDao.insertBooks(books)
    }

    override suspend fun deleteBooksForList(isbn13: String) {
        bookListDao.deleteBooksForList(isbn13)
    }

    override fun getAllBooks(): Flow<List<BookEntity>> {
        return bookListDao.getAllBooks()
    }

    /** Book Review */

    override suspend fun insertBookReview(bookReview: BookReviewEntity) {
        bookReviewDAO.insertBookReview(bookReview)
    }

    override suspend fun deleteBookReview(isbn13: String) {
        bookReviewDAO.deleteBookReview(isbn13)
    }

    override fun getBookReview(isbn13: String): Flow<BookReviewEntity> {
        return bookReviewDAO.getBookReview(isbn13)
    }

    /** DataStore */

    override suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences> {
        return dataStore.preferencesFlow
    }
}