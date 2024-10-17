package com.monsalud.bookshelf.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.monsalud.bookshelf.R
import com.monsalud.bookshelf.presentation.BookReviewState
import com.monsalud.bookshelf.presentation.BookshelfViewModel
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun BookshelfReviewScreen(
    isbn13: String,
    rank: Int,
    rankLastWeek: Int,
    weeksOnList: Int,
    publisher: String,
    bookImage: String,
    amazonProductUrl: String,
    author: String,
) {

    val viewModel: BookshelfViewModel = koinViewModel()
    val uriHandler = LocalUriHandler.current

    val bookReviewState by viewModel.bookReviewState.collectAsStateWithLifecycle()
    val isLoadingReview by viewModel.isLoadingReview.collectAsStateWithLifecycle()

    LaunchedEffect(isbn13) {
        viewModel.fetchBookReview(isbn13)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rank: $rank",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Text(
                    text = "Last Week's Rank: $rankLastWeek",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = "Weeks on List: $weeksOnList",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )

            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bookImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "Book Cover",
                placeholder = null,
                error = painterResource(id = R.drawable.error_image),
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit,
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            when (val state = bookReviewState) {
                is BookReviewState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                            .size(80.dp)
                    )
                }

                BookReviewState.Initial -> {
                    Text(
                        text = "Initial State",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is BookReviewState.Success -> {
                    val review = state.review

                    Text(
                        text = review.bookTitle,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 0.dp,
                            bottom = 0.dp
                        )
                    )
                    Text(
                        text = author,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 0.dp,
                            bottom = 0.dp
                        )
                    )
                    Text(
                        text = "Publisher: $publisher",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 0.dp
                        )
                    )
                    Text(
                        text = review.summary,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "Read the full New York Times review",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Blue,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                Timber.d("Opening book review link: ${review.url}")
                                uriHandler.openUri(review.url)
                            }
                    )
                }

                is BookReviewState.Error -> {
                    Text(
                        text = "Error loading review: ${state.message}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                BookReviewState.NoReview -> {
                    Text(
                        text = "No review available for this book.",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Text(
                text = "Buy this book on Amazon",
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSystemInDarkTheme()) Color.White else Color.Blue,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp)
                    .clickable {
                        uriHandler.openUri(amazonProductUrl)
                    }
            )
        }
    }
}

@Composable
@Preview
fun BookReviewScreenPreview() {
    BookshelfReviewScreen(
        isbn13 = "12345",
        rank = 1,
        rankLastWeek = 2,
        weeksOnList = 3,
        publisher = "Publisher",
        bookImage = "https://storage.googleapis.com/du-prd/books/images/9780593449592.jpg",
        amazonProductUrl = "",
        author = ""
    )
}
