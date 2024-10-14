package com.monsalud.bestsellerbookshelf.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.monsalud.bestsellerbookshelf.presentation.screens.BestSellersListScreen
import com.monsalud.bestsellerbookshelf.presentation.screens.BookReviewScreen
import kotlinx.serialization.Serializable

@Serializable
object BookListScreen

@Serializable
data class BookDetailScreen(
    val isbn: Int,
)

@Composable
fun BookshelfNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = BookListScreen,
    ) {
        composable<BookListScreen> {
            BestSellersListScreen(
                onClick = { isbn ->
                    navController.navigate(BookDetailScreen(isbn))
                }
            )
        }
        composable<BookDetailScreen> { entry ->
            val book = entry.toRoute<BookDetailScreen>()
            BookReviewScreen(book.isbn)
        }
    }
}