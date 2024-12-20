package com.monsalud.bookshelf.presentation.screens.listscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.monsalud.bookshelf.R
import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.presentation.components.BookItem
import com.monsalud.bookshelf.presentation.components.OnboardingDialog
import com.monsalud.bookshelf.ui.theme.spacing
import org.koin.androidx.compose.koinViewModel


@Composable
fun BookshelfListScreen(
    listName: String,
    onBookClick: (BookEntity) -> Unit,
) {
    val viewModel: BookshelfListViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showOnboardingDialog by viewModel.showOnboarding.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.syncAndObserveBookList(listName)
    }

    LaunchedEffect(uiState) {
        if (uiState is BookListState.Success || uiState is BookListState.Error) {
            isRefreshing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = {
                isRefreshing = true
                viewModel.syncAndObserveBookList(listName, true)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("bookshelfListColumn")
            ) {
                TitleSection(listName = listName)

                when (val state = uiState) {
                    is BookListState.Success -> {
                        if (state.bookList != null) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(MaterialTheme.spacing.small),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                            ) {
                                items(state.bookList.books) { book ->
                                    BookItem(
                                        book = book,
                                        onClick = { onBookClick(book) },
                                    )
                                }
                            }
                        }
                    }

                    is BookListState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .testTag("loadingIndicator")
                                    .padding(MaterialTheme.spacing.medium)
                                    .size(80.dp)
                            )
                        }
                    }

                    is BookListState.Refreshing -> {
                        // Swipe refresh handles loading indicator
                    }

                    is BookListState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.testTag("errorMessage")
                            )
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                            Text(
                                text = "Pull down to refresh",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.testTag("refreshMessage")
                            )
                        }
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
        }
    }
}


@Composable
fun TitleSection(listName: String) {
    Text(
        text = listName,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(
            top = MaterialTheme.spacing.medium,
            start = MaterialTheme.spacing.medium,
            end = MaterialTheme.spacing.medium,
            bottom = MaterialTheme.spacing.none
        )
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
}

@Composable
@Preview
fun BookshelfListScreenPreview() {
    BookshelfListScreen("Hardcover Fiction", onBookClick = {})
}
