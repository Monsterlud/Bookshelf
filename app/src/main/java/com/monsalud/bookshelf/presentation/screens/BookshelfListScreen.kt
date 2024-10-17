package com.monsalud.bookshelf.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.presentation.BookshelfViewModel
import com.monsalud.bookshelf.presentation.components.OnboardingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookshelfListScreen(
    listName: String,
    onBookClick: (String) -> Unit,
) {
    val viewModel: BookshelfViewModel = koinViewModel()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val userPreferences by viewModel.userPreferencesFlow.collectAsStateWithLifecycle()
    val isLoadingPreferences by viewModel.isLoadingPreferences.collectAsStateWithLifecycle()

    val showOnboardingDialog by remember {
        derivedStateOf {
            !isLoadingPreferences && !userPreferences.hasSeenOnboardingDialog
        }
    }

    val listWithBooks by viewModel.bookListFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setListName(listName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = listName,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )

            if (!isLoading && listWithBooks != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listWithBooks!!.books) { book ->
                        BookItem(
                            book = book,
                            onClick = { onBookClick(book.primaryIsbn13) },
                        )
                    }
                }
            } else if (!isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Data Available")
                }
            }
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp)
                    .padding(16.dp)
            )
        }
    }
    if (showOnboardingDialog) {
        OnboardingDialog(
            onDismiss = {
                viewModel.updateHasSeenOnboardingDialog(true)
            }
        )
    }
}

@Composable
fun BookItem(
    book: BookEntity,
    onClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(book.primaryIsbn13) }
            .padding(16.dp)
    ) {
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = book.author,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}


@Composable
@Preview
fun BookshelfListScreenPreview() {
    BookshelfListScreen("Hardcover Fiction", onBookClick = {})
}