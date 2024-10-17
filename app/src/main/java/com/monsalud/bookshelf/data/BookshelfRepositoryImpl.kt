package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import com.monsalud.bookshelf.data.remote.booklistapi.BookListResponseDTO
import com.monsalud.bookshelf.data.remote.booklistapi.toBookEntity
import com.monsalud.bookshelf.data.remote.booklistapi.toBookListEntity
import com.monsalud.bookshelf.domain.BookshelfRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class BookshelfRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : BookshelfRepository {

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

    override suspend fun refreshBookListInDB(listName: String) {
        Timber.d("listName when refreshing book list in repository: $listName")
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

    override suspend fun getBookReview(isbn: String): Flow<BookReviewEntity> {
        return localDataSource.getBookReview(isbn)
    }

    override suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences> {
        return localDataSource.getUserPreferencesFlow()
    }

    override suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        localDataSource.updateHasSeenOnboardingDialog(hasSeen)
    }
}