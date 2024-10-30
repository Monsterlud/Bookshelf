package com.monsalud.bookshelf.data.remote.bookreviewapi

/** DTOs to handle Book Reviews API data */

data class BookReviewResponseDto(
    val status: String,
    val copyright: String,
    val num_results: Int,
    val results: List<BookReviewDto>
)

data class BookReviewDto(
    val url: String,
    val publication_dt: String,
    val byline: String,
    val book_title: String,
    val book_author: String,
    val summary: String,
    val uuid: String,
    val uri: String,
    val isbn13: List<String>
)