package com.monsalud.bookshelf.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.monsalud.bookshelf.presentation.BookshelfViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookshelfListScreen(
    urlEndpoint: String,
    onClick: (Int) -> Unit
) {
    val viewModel: BookshelfViewModel = koinViewModel()

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllBooks()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick(6789) }
            .background(color = MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Best Sellers List Screen",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Text(
                text = "$urlEndpoint",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
            )

        }
    }
}

@Composable
@Preview
fun BookshelfListScreenPreview() {
    BookshelfListScreen("", onClick = {})
}