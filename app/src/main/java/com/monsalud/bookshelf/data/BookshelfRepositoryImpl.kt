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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BookshelfRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : BookshelfRepository {

    override suspend fun refreshBookListInDBFromAPI(listName: String) {
        try {
            val result = remoteDataSource.getBooksInListFromApi(listName)
            result.onSuccess { jsonString ->
                Timber.d("Received JSON String: $jsonString")
                if (jsonString != null) {
                    try {
                        val listWithBooks = parseBookListJsonToListWithBooks(jsonString)
                        Timber.d("Parsed JSON String to ListWithBooks: $listWithBooks")
                        localDataSource.deleteListWithBooks(listName)
                        localDataSource.insertListWithBooks(listWithBooks)
                    } catch (e: Exception) {
                        Timber.d("Error parsing JSON String: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
            result.onFailure {
                Timber.e("Error fetching book list: ${it.message}")
                throw IllegalStateException("Error fetching book list: ${it.message}")
            }
        } catch (e: Exception) {
            Timber.e("Error fetching book list: ${e.message}")
            throw IllegalStateException("Error fetching book list: ${e.message}")
        }
    }

    override suspend fun getListWithBooks(listName: String): Flow<ListWithBooks?> {
        return localDataSource.getListWithBooks(listName)
    }

    override suspend fun hasDataForList(listName: String): Boolean {
        return localDataSource.getListWithBooks(listName).first() != null
    }

    override suspend fun getBookReview(isbn13: String) : Flow<BookReviewEntity?> {
        return flow {
            Timber.d("Fetching book review from DB for ISBN: $isbn13")
            val localBookReview = localDataSource.getBookReview(isbn13).first()
            if (localBookReview != null && !shouldReplaceOldReview(localBookReview)) {
                Timber.d("Using cached book review for ISBN: $isbn13")
                emit(localBookReview)
            } else {
                val remoteBookReview = fetchAndSaveReview(isbn13)
                emit(remoteBookReview)
            }
        }
    }

    override suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences> {
        return localDataSource.getUserPreferencesFlow()
    }

    override suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        localDataSource.updateHasSeenOnboardingDialog(hasSeen)
    }


    /** Private Helper Functions */

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

    private suspend fun fetchAndSaveReview(isbn13: String) : BookReviewEntity? {
        val result = remoteDataSource.getBookReviewFromApi(isbn13)
        Timber.d("Fetching book review from API for ISBN: $isbn13")
        Timber.d("API Response: ${result.getOrNull()}")
        return result.getOrNull()?.let { jsonString ->
            parseBookReviewJsonToBookReviewEntity(jsonString)?.also {
                localDataSource.insertBookReview(it)
            }
        }
    }

    private fun shouldReplaceOldReview(review: BookReviewEntity) : Boolean {
        val oneDayAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        return review.timestamp < oneDayAgo
    }
}
