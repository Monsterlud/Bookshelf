package com.monsalud.bookshelf.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.monsalud.bookshelf.presentation.screens.BookshelfListScreen
import com.monsalud.bookshelf.presentation.screens.BookshelfReviewScreen
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
data class BookListScreen(
    val listName: String,
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
        startDestination = BookListScreen("Hardcover Fiction"),
        modifier = Modifier.padding(innerPadding),
    ) {
        composable<BookListScreen> { entry ->
            val screen = entry.toRoute<BookListScreen>()
            Timber.d("list name from navhost: ${screen.listName}")
            BookshelfListScreen(
                listName = screen.listName,
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