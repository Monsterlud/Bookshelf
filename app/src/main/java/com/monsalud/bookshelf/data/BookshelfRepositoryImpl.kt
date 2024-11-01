package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookListEntity
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import com.monsalud.bookshelf.data.remote.bookreviewapi.toBookReviewEntity
import com.monsalud.bookshelf.domain.BookshelfRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BookshelfRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val moshi: Moshi,
) : BookshelfRepository {

    /** Bookshelf List Repository Functions */

    override suspend fun refreshBookListInDbFromApi(listName: String): Result<Unit> {
        return try {
            remoteDataSource.getBooksInListFromApi(listName)
                .map { bookListResponseDto ->
                    val bookListResultDto = bookListResponseDto.results

                    if (bookListResultDto.books.isEmpty()) {
                        return Result.failure(Exception("No books found for list: $listName"))
                    }

                    val bookListEntity = BookListEntity(
                        listName = bookListResultDto.listName,
                        bestsellersDate = bookListResultDto.bestsellersDate,
                        publishedDate = bookListResultDto.publishedDate,
                        displayName = bookListResultDto.displayName,
                        normalListEndsAt = bookListResultDto.normalListEndsAt,
                        updated = bookListResultDto.updated,
                    )

                    val bookEntities = bookListResponseDto.results.books.map { bookDto ->
                        BookEntity(
                            primaryIsbn13 = bookDto.primaryIsbn13,
                            listName = bookListResultDto.listName,
                            rank = bookDto.rank,
                            rankLastWeek = bookDto.rankLastWeek,
                            weeksOnList = bookDto.weeksOnList,
                            asterisk = bookDto.asterisk,
                            dagger = bookDto.dagger,
                            primaryIsbn10 = bookDto.primaryIsbn10,
                            publisher = bookDto.publisher,
                            description = bookDto.description,
                            price = bookDto.price,
                            title = bookDto.title,
                            author = bookDto.author,
                            contributor = bookDto.contributor,
                            contributorNote = bookDto.contributorNote,
                            bookImage = bookDto.bookImage,
                            amazonProductUrl = bookDto.amazonProductUrl,
                            ageGroup = bookDto.ageGroup,
                            bookReviewLink = bookDto.bookReviewLink,
                            firstChapterLink = bookDto.firstChapterLink,
                            sundayReviewLink = bookDto.sundayReviewLink,
                            articleChapterLink = bookDto.articleChapterLink,
                        )
                    }

                    ListWithBooks(bookListEntity, bookEntities)

                }
                .fold(
                    onSuccess = { listWithBooks ->
                        try {
                            localDataSource.deleteListWithBooks(listName)
                            localDataSource.insertListWithBooks(listWithBooks)
                            Timber.d("Successfully refreshed $listName list in Database")
                            Result.success(Unit)
                        } catch (e: Exception) {
                            Timber.e("Database error refreshing $listName list: ${e.message}")
                            Result.failure(e)
                        }
                    },
                    onFailure = { error ->
                        Timber.e("Error refreshing $listName list in Database: ${error.message}")
                        Result.failure(error)
                    }
                )
        } catch (e: Exception) {
            Timber.e("Error refreshing $listName list in Database: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getListWithBooks(listName: String): Flow<ListWithBooks?> {
        return localDataSource.getListWithBooks(listName)
    }

    override suspend fun hasDataForList(listName: String): Boolean {
        return localDataSource.getListWithBooks(listName).first() != null
    }

    /** Book Review Repository Functions */

    override suspend fun getBookReview(isbn: String): Flow<Result<BookReviewEntity?>> {
        return flow {
            Timber.d("Fetching book review from DB for ISBN: $isbn")
            val localBookReview = localDataSource.getBookReview(isbn).first()
            if (localBookReview != null && !shouldReplaceOldReview(localBookReview)) {
                Timber.d("Using cached book review for ISBN: $isbn")
                emit(Result.success(localBookReview))
            } else {
                fetchAndSaveReview(isbn)
                    .onSuccess {
                        emit(Result.success(it))
                    }
                    .onFailure {
                        emit(Result.failure(it))
                    }
            }
        }
    }

    /** User Preferences Repository Functions */

    override suspend fun getUserPreferencesFlow(): Flow<BookshelfDataStore.UserPreferences> {
        return localDataSource.getUserPreferencesFlow()
    }

    override suspend fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        localDataSource.updateHasSeenOnboardingDialog(hasSeen)
    }


    /** Private Helper Functions */

    private suspend fun fetchAndSaveReview(isbn13: String): Result<BookReviewEntity?> {
        remoteDataSource.getBookReviewFromApi(isbn13)
            .map { bookReviewResponseDto ->
                val bookReviewDto = bookReviewResponseDto.results.firstOrNull()
                val bookReviewEntity = bookReviewDto?.toBookReviewEntity()
                bookReviewEntity?.let {
                    localDataSource.insertBookReview(it)
                }
                bookReviewEntity
            }
            .fold(
                onSuccess = { return Result.success(it) },
                onFailure = { return Result.failure(it) }
            )
    }

    private fun shouldReplaceOldReview(review: BookReviewEntity): Boolean {
        val oneDayAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        return review.timestamp < oneDayAgo
    }
}
