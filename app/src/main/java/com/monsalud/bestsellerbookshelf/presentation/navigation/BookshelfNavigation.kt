package com.monsalud.bestsellerbookshelf.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.monsalud.bestsellerbookshelf.presentation.screens.BookshelfListScreen
import com.monsalud.bestsellerbookshelf.presentation.screens.BookshelfReviewScreen
import kotlinx.serialization.Serializable

@Serializable
data class BookListScreen(
    val urlEndpoint: String = "https://api.nytimes.com/svc/books/v3/lists/full-overview.json",
)

@Serializable
data class BookDetailScreen(
    val isbn: Int,
)

@Composable
fun BookshelfNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = BookListScreen(),
    ) {
        composable<BookListScreen> { entry ->
            val screen = entry.toRoute<BookListScreen>()
            BookshelfListScreen(
                urlEndpoint = screen.urlEndpoint,
                onClick = { isbn ->
                    navController.navigate(BookDetailScreen(isbn))
                }
            )
        }
        composable<BookDetailScreen> { entry ->
            val book = entry.toRoute<BookDetailScreen>()
            BookshelfReviewScreen(book.isbn)
        }
    }
}