package com.monsalud.bookshelf.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.monsalud.bookshelf.R
import com.monsalud.bookshelf.data.local.room.BookEntity

@Composable
fun BookItem(
    book: BookEntity,
    onClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(book.primaryIsbn13) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = book.rank.toString(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(end = 8.dp),
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.bookImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "Cover of ${book.title}",
                placeholder = null,
                error = painterResource(id = R.drawable.error_image),
                modifier = Modifier.size(120.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(book.primaryIsbn13) }
                    .padding(8.dp)
            ) {
                Text(
                    text = "Weeks on List: ${book.weeksOnList}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(bottom = 0.dp)
                )
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(bottom = 0.dp)
                )
                Text(
                    text = book.contributor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(bottom = 0.dp)
                )
                Text(
                    text = book.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }


        }
    }
}

@Composable
@Preview
fun BookItemPreview() {
    BookItem(
        book = BookEntity(
            listName = "Hardcover Fiction",
            rank = 1,
            rankLastWeek = 1,
            weeksOnList = 3,
            asterisk = 0,
            dagger = 0,
            primaryIsbn10 = "0593449592",
            primaryIsbn13 = "9780593449592",
            publisher = "Random House",
            description = "A man in search of the father he never knew encounters a single mom and rumors circulate of the nearby appearance of a white deer.",
            price = 0,
            title = "COUNTING MIRACLES",
            author = "Nicholas Sparks",
            contributor = "by Nicholas Sparks",
            contributorNote = "",
            bookImage = "https://storage.googleapis.com/du-prd/books/images/9780593449592.jpg",
            amazonProductUrl = "",
            ageGroup = "",
            bookReviewLink = "",
            firstChapterLink = "",
            sundayReviewLink = "",
            articleChapterLink = "",
        )
    ) {}
}