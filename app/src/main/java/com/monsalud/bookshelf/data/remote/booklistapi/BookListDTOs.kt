package com.monsalud.bookshelf.data.remote.booklistapi

import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookListEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/** DTOs to handle Books List API data */

@JsonClass(generateAdapter = true)
data class BookListResponseDto(
    @Json(name = "status") val status: String,
    @Json(name = "copyright") val copyright: String,
    @Json(name = "num_results") val numResults: Int,
    @Json(name = "last_modified") val lastModified: String,
    @Json(name = "results") val results: BookListResultDto
)

@JsonClass(generateAdapter = true)
data class BookListResultDto(
    @Json(name = "list_name") val listName: String,
    @Json(name = "bestsellers_date") val bestsellersDate: String,
    @Json(name = "published_date") val publishedDate: String,
    @Json(name = "display_name") val displayName: String,
    @Json(name = "normal_list_ends_at") val normalListEndsAt: Int,
    @Json(name = "updated") val updated: String,
    @Json(name = "books") val books: List<BookDto>
)

@JsonClass(generateAdapter = true)
data class BookDto(
    @Json(name = "rank") val rank: Int,
    @Json(name = "rank_last_week") val rankLastWeek: Int,
    @Json(name = "weeks_on_list") val weeksOnList: Int,
    @Json(name = "asterisk") val asterisk: Int,
    @Json(name = "dagger") val dagger: Int,
    @Json(name = "primary_isbn10") val primaryIsbn10: String,
    @Json(name = "primary_isbn13") val primaryIsbn13: String,
    @Json(name = "publisher") val publisher: String,
    @Json(name = "description") val description: String,
    @Json(name = "price") val price: Int,
    @Json(name = "title") val title: String,
    @Json(name = "author") val author: String,
    @Json(name = "contributor") val contributor: String,
    @Json(name = "contributor_note") val contributorNote: String,
    @Json(name = "book_image") val bookImage: String,
    @Json(name = "amazon_product_url") val amazonProductUrl: String,
    @Json(name = "age_group") val ageGroup: String,
    @Json(name = "book_review_link") val bookReviewLink: String,
    @Json(name = "first_chapter_link") val firstChapterLink: String,
    @Json(name = "sunday_review_link") val sundayReviewLink: String,
    @Json(name = "article_chapter_link") val articleChapterLink: String,
)

fun BookListResultDto.toBookListEntity() = BookListEntity(
    listName = listName,
    bestsellersDate = bestsellersDate,
    publishedDate = publishedDate,
    displayName = displayName,
    normalListEndsAt = normalListEndsAt,
    updated = updated,
)

fun BookDto.toBookEntity(listName: String) = BookEntity(
    rank = rank,
    rankLastWeek = rankLastWeek,
    weeksOnList = weeksOnList,
    asterisk = asterisk,
    dagger = dagger,
    primaryIsbn10 = primaryIsbn10,
    primaryIsbn13 = primaryIsbn13,
    publisher = publisher,
    description = description,
    price = price,
    title = title,
    author = author,
    contributor = contributor,
    contributorNote = contributorNote,
    bookImage = bookImage,
    amazonProductUrl = amazonProductUrl,
    ageGroup = ageGroup,
    bookReviewLink = bookReviewLink,
    firstChapterLink = firstChapterLink,
    sundayReviewLink = sundayReviewLink,
    articleChapterLink = articleChapterLink,
    listName = listName
)
