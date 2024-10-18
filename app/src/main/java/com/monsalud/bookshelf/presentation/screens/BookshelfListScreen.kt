package com.monsalud.bookshelf.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monsalud.bookshelf.R
import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.presentation.BookshelfViewModel
import com.monsalud.bookshelf.presentation.components.BookItem
import com.monsalud.bookshelf.presentation.components.OnboardingDialog
import com.monsalud.bookshelf.ui.theme.spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookshelfListScreen(
    listName: String,
    onBookClick: (BookEntity) -> Unit,
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
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.medium,
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.none)
            )
            Text(
                text = stringResource(id = R.string.list_subtitle),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.none,
                    start = MaterialTheme.spacing.medium, end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.none
                )
            )

            if (!isLoading && listWithBooks != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.small),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                ) {
                    items(listWithBooks!!.books) { book ->
                        BookItem(
                            book = book,
                            onClick = { onBookClick(book) },
                        )
                    }
                }
            } else if (!isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.no_data_available))
                }
            }
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(MaterialTheme.spacing.medium)
                    .size(80.dp)
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
@Preview
fun BookshelfListScreenPreview() {
    BookshelfListScreen("Hardcover Fiction", onBookClick = {})
}
