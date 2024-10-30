package com.monsalud.bookshelf.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.monsalud.bookshelf.presentation.screens.listscreen.BookshelfListScreen
import com.monsalud.bookshelf.presentation.screens.reviewscreen.BookshelfReviewScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data class BookListScreen(
        val listName: String,
    )

    @Serializable
    data class BookDetailScreen(
        val isbn13: String,
        val rank: Int,
        val rankLastWeek: Int,
        val weeksOnList: Int,
        val publisher: String,
        val bookImage: String,
        val amazonProductUrl: String,
        val author: String,
        val title: String,
        val description: String,
    )
}

@Composable
fun BookshelfNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.BookListScreen("Hardcover Fiction"),
        modifier = Modifier.padding(innerPadding),
    ) {
        composable<Screen.BookListScreen> { entry ->
            val screen = entry.toRoute<Screen.BookListScreen>()
            BookshelfListScreen(
                listName = screen.listName,
                onBookClick = { book ->
                    navController.navigate(
                        Screen.BookDetailScreen(
                            isbn13 = book.primaryIsbn13,
                            rank = book.rank,
                            rankLastWeek = book.rankLastWeek,
                            weeksOnList = book.weeksOnList,
                            publisher = book.publisher,
                            bookImage = book.bookImage,
                            amazonProductUrl = book.amazonProductUrl,
                            author = book.author,
                            title = book.title,
                            description = book.description,
                        )
                    )
                }
            )
        }
        composable<Screen.BookDetailScreen> { entry ->
            val book = entry.toRoute<Screen.BookDetailScreen>()
            BookshelfReviewScreen(
                book.isbn13,
                book.rank,
                book.rankLastWeek,
                book.weeksOnList,
                book.publisher,
                book.bookImage,
                book.amazonProductUrl,
                book.author,
                book.title,
                book.description,
            )
        }
    }
}
