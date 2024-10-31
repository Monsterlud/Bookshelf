package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import com.monsalud.bookshelf.data.remote.mockBookListResponseDto
import com.monsalud.bookshelf.data.remote.mockEmptyBookListResponseDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class BookshelfRepositoryImplTest {

    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var repository: BookshelfRepositoryImpl

    @Before
    fun setUp() {
        localDataSource = mockk<LocalDataSource>()
        remoteDataSource = mockk<RemoteDataSource>()
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        repository = BookshelfRepositoryImpl(localDataSource, remoteDataSource, moshi)
    }

    @Test
    fun `refreshBookListInDBFromAPI should update local database on successful API call`() {
        runTest {
            // GIVEN
            val listName = "Hardcover Fiction"
            val bookListResponseDto = mockBookListResponseDto


            coEvery { remoteDataSource.getBooksInListFromApi(listName) } returns Result.success(
                bookListResponseDto
            )
            coEvery { localDataSource.deleteListWithBooks(any()) } just Runs
            coEvery { localDataSource.insertListWithBooks(any()) } just Runs

            // WHEN
            repository.refreshBookListInDbFromApi(listName)

            //THEN
            coVerify(exactly = 1) {
                remoteDataSource.getBooksInListFromApi(listName)
                localDataSource.deleteListWithBooks(listName)
                localDataSource.insertListWithBooks(any())
            }
        }
    }

    @Test
    fun `refreshBookListInDBFromAPI should not update database when API returns empty book list`() = runTest {
        // GIVEN
        val listName = "Hardcover Fiction"
        coEvery { remoteDataSource.getBooksInListFromApi(listName) } returns Result.success(
            mockEmptyBookListResponseDto
        )

        // WHEN
        repository.refreshBookListInDbFromApi(listName)

        // THEN
        coVerify(exactly = 1) { remoteDataSource.getBooksInListFromApi(listName) }
        coVerify(exactly = 0) {
            localDataSource.deleteListWithBooks(any())
            localDataSource.insertListWithBooks(any())
        }
    }

    @Test
    fun `refreshBookListInDBFromAPI should log error on API failure`() = runTest {
        // GIVEN
        val listName = "Hardcover Fiction"
        val exception = Exception("Network Error")
        coEvery { remoteDataSource.getBooksInListFromApi(listName) } returns Result.failure(exception)

        // WHEN
        repository.refreshBookListInDbFromApi(listName)

        // THEN
        coVerify(exactly = 1) { remoteDataSource.getBooksInListFromApi(listName) }
        coVerify(exactly = 0) {
            localDataSource.deleteListWithBooks(any())
            localDataSource.insertListWithBooks(any())
        }
    }

    @Test
    fun `getListWithBooks should return Flow from localDataSource`() {
        runTest {
            // GIVEN
            val listName = "Hardcover Fiction"
            val mockListWithBooks = mockk<ListWithBooks>()
            coEvery { localDataSource.getListWithBooks(listName) } returns flowOf(mockListWithBooks)

            // WHEN
            val result = repository.getListWithBooks(listName)

            // THEN
            assertEquals(mockListWithBooks, result.first())
            coVerify(exactly = 1) { localDataSource.getListWithBooks(listName) }
        }
    }

    @Test
    fun `getListWithBooks should return null Flow when data doesn't exist`() {
        runTest {
            // GIVEN
            val listName = "Nonexistent List"
            coEvery { localDataSource.getListWithBooks(listName) } returns flowOf(null)

            // WHEN
            val result = repository.getListWithBooks(listName)

            // THEN
            assertNull(result.first())
            coVerify(exactly = 1) { localDataSource.getListWithBooks(listName) }
        }
    }

    @Test
    fun `getListWithBooks should propagate exceptions from localDataSource`() {
        runTest {
            // GIVEN
            val listName = "Exception List"
            val exception = RuntimeException("Database Exception")
            coEvery { localDataSource.getListWithBooks(listName) } throws exception

            // WHEN THEN
            try {
                repository.getListWithBooks(listName)
            } catch (e: Exception) {
                assertEquals(exception, e)
            }
            coVerify(exactly = 1) { localDataSource.getListWithBooks(listName) }
        }
    }

    @Test
    fun `getListWithBooks should return empty Flow for empty list name`() {
        runTest {
            // GIVEN
            val emptyListName = ""
            coEvery { localDataSource.getListWithBooks(emptyListName) } returns flowOf(null)

            // WHEN
            val result = repository.getListWithBooks(emptyListName)

            // THEN
            assertNull(result.first())
            coVerify(exactly = 1) { localDataSource.getListWithBooks(emptyListName) }
        }
    }

    @Test
    fun `getListWithBooks should handle very long list names`() {
        runTest {
            // GIVEN
            val veryLongListName = "A".repeat(1000)
            val mockListWithBooks = mockk<ListWithBooks>()
            coEvery { localDataSource.getListWithBooks(veryLongListName) } returns flowOf(
                mockListWithBooks
            )

            // WHEN
            val result = repository.getListWithBooks(veryLongListName)

            // THEN
            assertEquals(mockListWithBooks, result.first())
            coVerify(exactly = 1) { localDataSource.getListWithBooks(veryLongListName) }
        }
    }

    @Test
    fun `getBookReview should return local review if cached and not outdated`() {
        runTest {
            // GIVEN
            val isbn13 = "9780743273565"
            val mockLocalReview = BookReviewEntity(
                bookIsbn13 = isbn13,
                url = "url",
                publicationDt = "publication",
                byline = "by Author",
                bookTitle = "A Really Great Book",
                bookAuthor = "Author",
                summary = "summary",
                timestamp = System.currentTimeMillis()
            )
            every { localDataSource.getBookReview(isbn13) } returns flowOf(mockLocalReview)

            // WHEN
            val result = repository.getBookReview(isbn13).first()

            // THEN
            assertEquals(Result.success(mockLocalReview), result)
            coVerify(exactly = 1) { localDataSource.getBookReview(isbn13) }
            coVerify(exactly = 0) { remoteDataSource.getBookReviewFromApi(any()) }
        }
    }
//
    @Test
    fun `getBookReview should emit failure if remote review is null`() {
        runTest {
            // GIVEN
            val isbn13 = "9780743273565"
            every { localDataSource.getBookReview(isbn13) } returns flowOf(null)
            coEvery { remoteDataSource.getBookReviewFromApi(isbn13) } returns Result.failure(Exception())

            // WHEN
            val result = repository.getBookReview(isbn13).first()

            // THEN
            assertTrue(result.isFailure)
            coVerify(exactly = 1) {
                localDataSource.getBookReview(isbn13)
                remoteDataSource.getBookReviewFromApi(isbn13)
            }
            coVerify(exactly = 0) { localDataSource.insertBookReview(any()) }
        }
    }

    @Test
    fun `getBookReview should emit failure if remote review fetch fails`() {
        runTest {
            // GIVEN
            val isbn13 = "9780743273565"
            val networkError = IOException("Network error")
            every { localDataSource.getBookReview(isbn13) } returns flowOf(null)
            coEvery { remoteDataSource.getBookReviewFromApi(isbn13) } returns Result.failure(networkError)

            // WHEN
            val result = repository.getBookReview(isbn13).first()

            // THEN
            assertTrue(result.isFailure)
            assertEquals(networkError, result.exceptionOrNull())
            coVerify(exactly = 1) {
                localDataSource.getBookReview(isbn13)
                remoteDataSource.getBookReviewFromApi(isbn13)
            }
            coVerify(exactly = 0) { localDataSource.insertBookReview(any()) }
        }
    }

    @Test
    fun `getBookReview should handle exception during remote review fetch`() {
        runTest {
            // GIVEN
            val isbn13 = "9780743273565"
            every { localDataSource.getBookReview(isbn13) } returns flowOf(null)
            coEvery { remoteDataSource.getBookReviewFromApi(isbn13) } throws Exception("Unexpected error")

            // WHEN
            val result = repository.getBookReview(isbn13)

            // THEN
            try {
                result.first()
            } catch (e: Exception) {
                assertEquals("Unexpected error", e.message)
            }
        }
    }

    @Test
    fun `getUserPreferencesFlow should return user preferences from local data source`() {
        runTest {
            // GIVEN
            val mockPreferences = BookshelfDataStore.UserPreferences(
                hasSeenOnboardingDialog = true,
            )
            coEvery { localDataSource.getUserPreferencesFlow() } returns flowOf(mockPreferences)

            // WHEN
            val result = repository.getUserPreferencesFlow()

            // THEN
            assertEquals(mockPreferences, result.first())
        }
    }

    @Test
    fun `updateHasSeenOnboardingDialog should update user preferences in local data source`() {
        runTest {
            // GIVEN
            val hasSeen = true
            coEvery { localDataSource.updateHasSeenOnboardingDialog(hasSeen) } returns Unit

            // WHEN
            repository.updateHasSeenOnboardingDialog(hasSeen)

            // THEN
            coVerify(exactly = 1) { localDataSource.updateHasSeenOnboardingDialog(hasSeen) }
        }
    }
}




