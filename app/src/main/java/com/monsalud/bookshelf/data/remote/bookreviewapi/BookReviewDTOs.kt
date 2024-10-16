package com.monsalud.bookshelf.data.remote.bookreviewapi

/** DTOs to handle Book Reviews API data */

data class BookReviewDTO(
    val url: String,
    val publication_dt: String,
    val byline: String,
    val book_title: String,
    val book_author: String,
    val summary: String,
    val isbn13: List<String>,
)