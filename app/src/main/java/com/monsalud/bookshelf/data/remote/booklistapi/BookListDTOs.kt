package com.monsalud.bookshelf.data.remote.booklistapi

import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookListEntity
import com.squareup.moshi.JsonClass


/** DTOs to handle Books List API data */

@JsonClass(generateAdapter = true)
data class BookListResponseDTO(
    val status: String,
    val copyright: String,
    val num_results: Int,
    val last_modified: String,
    val results: BookListResultDTO
)

@JsonClass(generateAdapter = true)
data class BookListResultDTO(
    val list_name: String,
    val bestsellers_date: String,
    val published_date: String,
    val display_name: String,
    val normal_list_ends_at: Int,
    val updated: String,
    val books: List<BookDTO>
)

@JsonClass(generateAdapter = true)
data class BookDTO(
    val rank: Int,
    val rank_last_week: Int,
    val weeks_on_list: Int,
    val asterisk: Int,
    val dagger: Int,
    val primary_isbn10: String,
    val primary_isbn13: String,
    val publisher: String,
    val description: String,
    val price: Int,
    val title: String,
    val author: String,
    val contributor: String,
    val contributor_note: String,
    val book_image: String,
    val amazon_product_url: String,
    val age_group: String,
    val book_review_link: String,
    val first_chapter_link: String,
    val sunday_review_link: String,
    val article_chapter_link: String,
)

fun BookListResultDTO.toBookListEntity() = BookListEntity(
    listName = list_name,
    bestsellersDate = bestsellers_date,
    publishedDate = published_date,
    displayName = display_name,
    normalListEndsAt = normal_list_ends_at,
    updated = updated,
)

fun BookDTO.toBookEntity(listName: String) = BookEntity(
    rank = rank,
    rankLastWeek = rank_last_week,
    weeksOnList = weeks_on_list,
    asterisk = asterisk,
    dagger = dagger,
    primaryIsbn10 = primary_isbn10,
    primaryIsbn13 = primary_isbn13,
    publisher = publisher,
    description = description,
    price = price,
    title = title,
    author = author,
    contributor = contributor,
    contributorNote = contributor_note,
    bookImage = book_image,
    amazonProductUrl = amazon_product_url,
    ageGroup = age_group,
    bookReviewLink = book_review_link,
    firstChapterLink = first_chapter_link,
    sundayReviewLink = sunday_review_link,
    articleChapterLink = article_chapter_link,
    listName = listName
)
