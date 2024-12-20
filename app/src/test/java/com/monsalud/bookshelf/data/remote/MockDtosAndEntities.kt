package com.monsalud.bookshelf.data.remote

import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookListEntity
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import com.monsalud.bookshelf.data.remote.booklistapi.BookDto
import com.monsalud.bookshelf.data.remote.booklistapi.BookListResponseDto
import com.monsalud.bookshelf.data.remote.booklistapi.BookListResultDto

val mockBookListResponseDto = BookListResponseDto(
    status = "OK",
    copyright = "Copyright 2024",
    numResults = 1,
    lastModified = "2023-10-26T15:38:13-04:00",
    results = BookListResultDto(
        listName = "Hardcover Fiction",
        bestsellersDate = "2024-10-31",
        publishedDate = "2024-10-31",
        displayName = "Hardcover Fiction",
        normalListEndsAt = 1,
        updated = "2023-10-26T15:38:13-04:00",
        books = listOf(
            BookDto(
                rank = 1,
                rankLastWeek = 2,
                weeksOnList = 5,
                asterisk = 0,
                dagger = 0,
                primaryIsbn10 = "0743273567",
                primaryIsbn13 = "9780743273565",
                publisher = "Publisher A",
                description = "A thrilling novel about adventure.",
                price = "25.99",
                title = "The Secret Island",
                author = "John Smith",
                contributor = "Contributor A",
                contributorNote = "Note about contributor",
                bookImage = "https://example.com/image1.jpg",
                amazonProductUrl = "https://www.amazon.com/dp/0743273567",
                ageGroup = "Adult",
                bookReviewLink = "https://example.com/review1",
                firstChapterLink = "https://example.com/chapter1",
                sundayReviewLink = "https://example.com/sunday_review1",
                articleChapterLink = "https://example.com/article1"
            ),
            BookDto(
                rank = 3,
                rankLastWeek = 5,
                weeksOnList = 10,
                asterisk = 1,
                dagger = 0,
                primaryIsbn10 = "1451648537",
                primaryIsbn13 = "9781451648533",
                publisher = "Publisher B",
                description = "A heartwarming story about friendship.",
                price = "19.99",
                title = "The Lost Puppy",
                author = "Jane Doe",
                contributor = "Contributor B",
                contributorNote = "Another note",
                bookImage = "https://example.com/image2.jpg",
                amazonProductUrl = "https://www.amazon.com/dp/1451648537",
                ageGroup = "Children",
                bookReviewLink = "https://example.com/review2",
                firstChapterLink = "https://example.com/chapter2",
                sundayReviewLink = "https://example.com/sunday_review2",
                articleChapterLink = "https://example.com/article2"
            )
        )
    )
)

val mockEmptyBookListResponseDto = BookListResponseDto(
    status = "OK",
    copyright = "Copyright (c) 2023 The New York Times Company.  All Rights Reserved.",
    numResults = 0,
    lastModified = "2024-10-31",
    results = BookListResultDto(
        listName = "",
        bestsellersDate = "",
        publishedDate = "",
        displayName = "",
        normalListEndsAt = 0,
        updated = "",
        books = emptyList()
    )
)

val mockBookReviewEntity = BookReviewEntity(
    bookIsbn13 = "9780743273565",
    url = "https://www.amazon.com/dp/0743273567",
    publicationDt = "2024-10-31",
    byline = "by David Bowie",
    bookTitle = "The Man Who Fell To Earth",
    bookAuthor = "David Bowie",
    summary = "A cool book about the man who fell to earth"
)

val mockListWithBooks =
    ListWithBooks(
        bookList = BookListEntity(
            listName = "Hardcover Fiction",
            bestsellersDate = "2024-10-31",
            publishedDate = "2024-10-31",
            displayName = "Hardcover Fiction",
            normalListEndsAt = 1,
            updated = "2023-10-26T15:38:13-04:00",
        ),
        books = listOf(
            BookEntity(
                primaryIsbn13 = "9780743273565",
                listName = "Hardcover Fiction",
                rank = 1,
                rankLastWeek = 1,
                weeksOnList = 1,
                asterisk = 0,
                dagger = 0,
                primaryIsbn10 = "0743273567",
                publisher = "Publisher",
                description = "Description",
                price = "10.00",
                title = "Title",
                author = "Author",
                contributor = "Contributor",
                contributorNote = "Contributor Note",
                bookImage = "Book Image",
                amazonProductUrl = "Amazon Product Url",
                ageGroup = "Age Group",
                bookReviewLink = "Book Review Link",
                firstChapterLink = "First Chapter Link",
                sundayReviewLink = "Sunday Review Link",
                articleChapterLink = "Article Chapter Link"
            ),
            BookEntity(
                primaryIsbn13 = "9780743273566",
                listName = "Hardcover Fiction",
                rank = 1,
                rankLastWeek = 1,
                weeksOnList = 1,
                asterisk = 0,
                dagger = 0,
                primaryIsbn10 = "0743273568",
                publisher = "Publisher",
                description = "Description",
                price = "10.00",
                title = "Title",
                author = "Author",
                contributor = "Contributor",
                contributorNote = "Contributor Note",
                bookImage = "Book Image",
                amazonProductUrl = "Amazon Product Url",
                ageGroup = "Age Group",
                bookReviewLink = "Book Review Link",
                firstChapterLink = "First Chapter Link",
                sundayReviewLink = "Sunday Review Link",
                articleChapterLink = "Article Chapter Link"
            )
        )
    )