package com.monsalud.bookshelf.data.remote.booklistapi

import com.monsalud.bookshelf.data.local.room.BookEntity
import com.monsalud.bookshelf.data.local.room.BookListEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/** DTOs to handle Books List API data */

@Serializable
data class BookListResponseDto(
    @SerialName("status") val status: String,
    @SerialName("copyright") val copyright: String,
    @SerialName("num_results") val numResults: Int,
    @SerialName("last_modified") val lastModified: String,
    @SerialName("results") val results: BookListResultDto
)

@Serializable
data class BookListResultDto(
    @SerialName("list_name") val listName: String,
    @SerialName("bestsellers_date") val bestsellersDate: String,
    @SerialName("published_date") val publishedDate: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("normal_list_ends_at") val normalListEndsAt: Int,
    @SerialName("updated") val updated: String,
    @SerialName("books") val books: List<BookDto>
)

@Serializable
data class BookDto(
    @SerialName("rank") val rank: Int,
    @SerialName("rank_last_week") val rankLastWeek: Int,
    @SerialName("weeks_on_list") val weeksOnList: Int,
    @SerialName("asterisk") val asterisk: Int,
    @SerialName("dagger") val dagger: Int,
    @SerialName("primary_isbn10") val primaryIsbn10: String,
    @SerialName("primary_isbn13") val primaryIsbn13: String,
    @SerialName("publisher") val publisher: String,
    @SerialName("description") val description: String,
    @SerialName("price") val price: String,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("contributor") val contributor: String,
    @SerialName("contributor_note") val contributorNote: String,
    @SerialName("book_image") val bookImage: String,
    @SerialName("amazon_product_url") val amazonProductUrl: String,
    @SerialName("age_group") val ageGroup: String,
    @SerialName("book_review_link") val bookReviewLink: String,
    @SerialName("first_chapter_link") val firstChapterLink: String,
    @SerialName("sunday_review_link") val sundayReviewLink: String,
    @SerialName("article_chapter_link") val articleChapterLink: String,
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

@Serializable
data class BookListErrorResponse(
    @SerialName("fault") val fault: Fault?
)

@Serializable
data class Fault(
    @SerialName("faultstring") val faultString: String?,
    @SerialName("detail") val detail: FaultDetail?
)

@Serializable
data class FaultDetail(
    @SerialName("errorcode") val errorCode: String?
)
