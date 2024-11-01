package com.monsalud.bookshelf

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import com.monsalud.bookshelf.presentation.screens.listscreen.BookListState
import com.monsalud.bookshelf.presentation.screens.listscreen.BookshelfListViewModel
import com.monsalud.bookshelf.presentation.screens.listscreen.BookshelfListScreen
import com.monsalud.bookshelf.ui.theme.BookshelfTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class BookShelfLIstScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: BookshelfListViewModel

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
    fun testInitialLoadingState() {
        val uiState = MutableStateFlow<BookListState>(BookListState.Loading)
        val showOnboarding = MutableStateFlow(false)

        every { mockViewModel.uiState } returns uiState.asStateFlow()
        every { mockViewModel.showOnboarding } returns showOnboarding.asStateFlow()

        composeTestRule.setContent {
            BookshelfTheme(darkTheme = false) {
                BookshelfListScreen(
                    listName = "Hardcover Fiction",
                    onBookClick = { },
                )
            }
        }

        composeTestRule.onNodeWithTag("loadingIndicator").assertExists()
    }

    @Test
    fun testCardExists_whenBookListIsNotEmpty() {
        val mockBook = mockBookEntity
        val mockList = ListWithBooks( mockBookListEntity, listOf(mockBook))

        val uiState = MutableStateFlow<BookListState>(
            BookListState.Success(bookList = mockList)
        )
        val showOnboarding = MutableStateFlow(false)

        every { mockViewModel.uiState } returns uiState.asStateFlow()
        every { mockViewModel.showOnboarding } returns showOnboarding.asStateFlow()

        composeTestRule.setContent {
            BookshelfTheme(darkTheme = false) {
                BookshelfListScreen(
                    listName = "Hardcover Fiction",
                    onBookClick = { },
                )
            }
        }

        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithTag("bookItemCard")[0].isDisplayed()
        }
    }
}