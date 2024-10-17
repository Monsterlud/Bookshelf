package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import com.monsalud.bookshelf.data.remote.booklistapi.BookListResponseDTO
import com.monsalud.bookshelf.data.remote.booklistapi.toBookEntity
import com.monsalud.bookshelf.data.remote.booklistapi.toBookListEntity
import com.monsalud.bookshelf.data.remote.bookreviewapi.BookReviewResponseDTO
import com.monsalud.bookshelf.domain.BookshelfRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class BookshelfRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : BookshelfRepository {


    override suspend fun refreshBookListInDB(listName: String) {
        val result = remoteDataSource.getBooksInListFromApi(listName)
        result.onSuccess { jsonString ->
            if (jsonString != null) {
                val listWithBooks = parseBookListJsonToListWithBooks(jsonString)
                localDataSource.deleteListWithBooks(listName)
                localDataSource.insertListWithBooks(listWithBooks)
            }
        }
        result.onFailure {
            Timber.e("Error fetching book list: ${it.message}")
            throw IllegalStateException("Error fetching book list: ${it.message}")
        }
    }

    override suspend fun getListWithBooks(listName: String): Flow<ListWithBooks?> {
        return localDataSource.getListWithBooks(listName)
    }

    override suspend fun getBookReviewFromApiAndSaveInDB(isbn: String): Flow<BookReviewEntity?> {
        return flow {
            Timber.d("Fetching book review from API for ISBN: $isbn")
            val result = remoteDataSource.getBookReviewFromApi(isbn)
            result.onSuccess { jsonString ->
                Timber.d("Received JSON string for ISBN: $isbn")
                if (jsonString != null) {
                    val bookReview = parseBookReviewJsonToBookReviewEntity(jsonString)
                    if (bookReview != null) {
                        Timber.d("Book review received for ISBN: $isbn")
                        localDataSource.deleteBookReview(bookReview.bookIsbn13)
                        localDataSource.insertBookReview(bookReview)
                        this@flow.emit(bookReview)
                    } else {
                        Timber.d("No book review available for ISBN: $isbn")
                        this@flow.emit(null)
                    }
                } else {
                    Timber.d("Null JSON string received for ISBN: $isbn")
                    this@flow.emit(null)
                }
            }.onFailure {
                Timber.e(it, "Error fetching book review from API for ISBN: $isbn")
                this@flow.emit(null)
            }
        }
    }

    override suspend fun getBookReview(isbn: String): Flow<BookReviewEntity?> {
        Timber.d("Fetching book review from DB for ISBN: $isbn")
        return localDataSource.getBookReview(isbn)
    }

    override suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences> {
        return localDataSource.getUserPreferencesFlow()
    }

    override suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        localDataSource.updateHasSeenOnboardingDialog(hasSeen)
    }

    private fun parseBookListJsonToListWithBooks(jsonString: String): ListWithBooks {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter(BookListResponseDTO::class.java)
        val apiResponse =
            adapter.fromJson(jsonString) ?: throw IllegalArgumentException("Invalid JSON string")
        val bookListEntity = apiResponse.results.toBookListEntity()
        val bookEntities =
            apiResponse.results.books.map { it.toBookEntity(apiResponse.results.list_name) }

        return ListWithBooks(bookListEntity, bookEntities)
    }

    private fun parseBookReviewJsonToBookReviewEntity(jsonString: String): BookReviewEntity? {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter(BookReviewResponseDTO::class.java)
        val apiResponse =
            adapter.fromJson(jsonString) ?: throw IllegalArgumentException("Invalid JSON string")
        if (apiResponse.results.isEmpty()) {
            Timber.d("No results found in the API response")
            return null
        }
        val bookReviewDTO = apiResponse.results.first()
        return BookReviewEntity(
            bookIsbn13 = bookReviewDTO.isbn13.firstOrNull() ?: "",
            url = bookReviewDTO.url,
            publicationDt = bookReviewDTO.publication_dt,
            byline = bookReviewDTO.byline,
            bookTitle = bookReviewDTO.book_title,
            bookAuthor = bookReviewDTO.book_author,
            summary = bookReviewDTO.summary
        )
    }
}
