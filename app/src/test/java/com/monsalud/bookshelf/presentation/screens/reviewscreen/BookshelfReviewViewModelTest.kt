package com.monsalud.bookshelf.presentation.screens.reviewscreen

import app.cash.turbine.test
import com.monsalud.bookshelf.MainDispatcherRule
import com.monsalud.bookshelf.data.FakeRepository
import com.monsalud.bookshelf.data.remote.mockBookReviewEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookshelfReviewViewModelTest {

    private lateinit var repository: FakeRepository
    private lateinit var viewModel: BookshelfReviewViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        repository = FakeRepository()
        viewModel = BookshelfReviewViewModel(repository)
    }

    @Test
    fun `fetchBookReview should update state to Success when review is found`() = runTest {
        // GIVEN
        val isbn = "9780743273565"
        val bookReviewEntity = mockBookReviewEntity
        repository.setBookReviewSuccess(bookReviewEntity)

        // WHEN
        viewModel.fetchBookReview(isbn)

        // THEN
        viewModel.bookReviewState.test {
            assertEquals(BookReviewState.Initial, awaitItem())
            assertEquals(BookReviewState.Loading, awaitItem())
            assertEquals(BookReviewState.Success(bookReviewEntity), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchBookReview should update state to NoReview when review is not found`() = runTest {
        // GIVEN
        val isbn13 = "9780743273565"
        repository.setBookReviewEmpty()

        // WHEN
        viewModel.fetchBookReview(isbn13)

        // THEN
        viewModel.bookReviewState.test {
            assertEquals(BookReviewState.Initial, awaitItem())
            assertEquals(BookReviewState.Loading, awaitItem())
            assertEquals(BookReviewState.NoReview, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchBookReview should update state to Error when an exception occurs`() = runTest {
        // GIVEN
        val isbn13 = "9780743273565"
        repository.setBookReviewFailure(Exception())

        // WHEN
        viewModel.fetchBookReview(isbn13)

        // THEN
        viewModel.bookReviewState.test {
            assertEquals(BookReviewState.Initial, awaitItem())
            assertEquals(BookReviewState.Loading, awaitItem())
            assertEquals(BookReviewState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
