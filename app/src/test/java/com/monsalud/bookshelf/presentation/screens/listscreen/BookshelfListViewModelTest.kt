package com.monsalud.bookshelf.presentation.screens.listscreen


import com.monsalud.bookshelf.MainDispatcherRule
import com.monsalud.bookshelf.data.FakeRepository
import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.remote.mockListWithBooks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookshelfListViewModelTest {

    private lateinit var viewModel: BookshelfListViewModel
    private lateinit var repository: FakeRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        repository = FakeRepository()
        viewModel = BookshelfListViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateHasSeenOnboardingDialog updates user preferences`() = runTest {
        // GIVEN
        repository.setUserPreferences(
            BookshelfDataStore.UserPreferences(hasSeenOnboardingDialog = false)
        )

        // WHEN
        viewModel.updateHasSeenOnboardingDialog(true)
        advanceUntilIdle()

        // THEN
        assertTrue(repository.updateHasSeenOnboardingDialogCalled)
        assertEquals(true, repository.lastHasSeenOnboardingDialogValue)
    }

    @Test
    fun `syncAndObserveBookList starts with Loading state when no cached data`() = runTest {
        // GIVEN
        repository.setHasDataForListResult(false)

        // WHEN
        viewModel.syncAndObserveBookList("Hardcover Fiction")
        advanceUntilIdle()

        // THEN
        assertTrue(viewModel.uiState.value is BookListState.Loading)
    }

    @Test
    fun `syncAndObserveBookList shows Success state when data exists in database`() = runTest {
        // GIVEN
        val mockList = mockListWithBooks
        repository.setHasDataForListResult(true)
        repository.setGetListWithBooksResult(mockList)

        // WHEN
        viewModel.syncAndObserveBookList("Hardcover Fiction")
        advanceUntilIdle()

        // THEN
        assertTrue(viewModel.uiState.value is BookListState.Success)
        assertEquals(mockList, (viewModel.uiState.value as BookListState.Success).bookList)
    }

    @Test
    fun `syncAndObserveBookList shows network error when offline`() = runTest {
        // GIVEN
        repository.setHasDataForListResult(false)
        repository.setShouldSimulateNetworkError(true)

        // WHEN
        viewModel.syncAndObserveBookList("Hardcover Fiction")
        advanceUntilIdle()

        // THEN
        assertTrue(viewModel.uiState.value is BookListState.Error)
        assertEquals(
            "Please check your internet connection and try again",
            (viewModel.uiState.value as BookListState.Error).message
        )
    }
}