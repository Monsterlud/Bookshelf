package com.monsalud.bookshelf

import androidx.room.PrimaryKey
import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookListEntity
import com.monsalud.bookshelf.data.local.room.BookReviewEntity

val mockBookEntity = BookEntity(
    primaryIsbn13 = "1234567890123",
    listName = "Hardcover Fiction",
    rank = 1,
    rankLastWeek = 2,
    weeksOnList = 3,
    asterisk = 0,
    dagger = 0,
    primaryIsbn10 = "1234567890",
    publisher = "HarperCollins",
    description = "A classic novel by F. Scott Fitzgerald",
    price = "13.00",
    title = "The Great Gatsby",
    author = "F. Scott Fitzgerald",
    contributor = "Contributor 1",
    contributorNote = "Note 1",
    bookImage = "https://example.com/book_image.jpg",
    amazonProductUrl = "https://example.com/amazon_product",
    ageGroup = "Adult",
    bookReviewLink = "https://example.com/book_review",
    firstChapterLink = "https://example.com/first_chapter",
    sundayReviewLink = "https://example.com/sunday_review",
    articleChapterLink = "https://example.com/article_chapter",
)

val mockBookListEntity = BookListEntity(
    listName = "Hardcover Fiction",
    bestsellersDate = "date",
    publishedDate = "date",
    displayName = "Hardcover Fiction",
    normalListEndsAt = 1,
    updated = "date",
)

val mockBookReviewEntity = BookReviewEntity(
    bookIsbn13 = "1234567890123",
    url = "https://example.com/book_review",
    publicationDt = "date",
    byline = "Byline",
    bookTitle = "The Great Gatsby",
    bookAuthor = "F. Scott Fitzgerald",
    summary = "Summary",
    timestamp = System.currentTimeMillis()
)
