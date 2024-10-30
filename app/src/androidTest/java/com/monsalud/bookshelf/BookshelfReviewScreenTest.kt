package com.monsalud.bookshelf

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.monsalud.bookshelf.presentation.screens.reviewscreen.BookReviewState
import com.monsalud.bookshelf.presentation.screens.listscreen.BookshelfListViewModel
import com.monsalud.bookshelf.presentation.screens.reviewscreen.BookshelfReviewScreen
import com.monsalud.bookshelf.presentation.screens.reviewscreen.BookshelfReviewViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class BookshelfReviewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: BookshelfReviewViewModel

    @Before
    fun setUp() {
        mockViewModel = mockk(relaxed = true)

        stopKoin()
        startKoin {
            modules(module {
                viewModel { mockViewModel }
            }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun bookReviewLoaded_shouldDisplayBookDetails() {

        every { mockViewModel.bookReviewState } returns MutableStateFlow(BookReviewState.Success(mockBookReviewEntity))

        composeTestRule.setContent {
            BookshelfReviewScreen(
                isbn13 = "9780345339701",
                rank = 1,
                rankLastWeek = 2,
                weeksOnList = 3,
                publisher = "Publisher",
                bookImage = "https://example.com/book_image.jpg",
                amazonProductUrl = "https://example.com/amazon_product",
                author = "F. Scott Fitzgerald",
                title = "The Great Gatsby",
                description = "A novel about the decadence and excess of the Jazz Age"
            )
        }
        composeTestRule.onNodeWithText("The Great Gatsby").assertIsDisplayed()
        composeTestRule.onNodeWithText("F. Scott Fitzgerald").assertIsDisplayed()
        composeTestRule.onNodeWithText("A novel about the decadence and excess of the Jazz Age").assertIsDisplayed()
    }
}