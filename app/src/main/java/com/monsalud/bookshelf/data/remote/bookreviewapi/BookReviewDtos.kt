package com.monsalud.bookshelf.data.remote.bookreviewapi

import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

/** DTOs to handle Book Reviews API data */

@Serializable
data class BookReviewResponseDto(
    @SerialName("status") val status: String,
    @SerialName("copyright") val copyright: String,
    @SerialName("num_results") val numResults: Int,
    @SerialName("results") val results: List<BookReviewDto> = emptyList(),
)

@Serializable
data class BookReviewDto(
    @SerialName("url") val url: String?,
    @SerialName("publication_dt") val publicationDt: String?,
    @SerialName("byline") val byline: String?,
    @SerialName("book_title") val bookTitle: String?,
    @SerialName("book_author") val bookAuthor: String?,
    @SerialName("summary") val summary: String?,
    @SerialName("uuid") val uuid: String?,
    @SerialName("uri") val uri: String?,
    @SerialName("isbn13") val isbn13: List<String>?,
)

@Serializable
data class BookReviewErrorResponse(
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

fun BookReviewDto.toBookReviewEntity() : BookReviewEntity? {
    return if (isbn13?.isNotEmpty() == true) {
        BookReviewEntity(
            bookIsbn13 = isbn13.first(),
            url = url ?: "",
            publicationDt = publicationDt ?: "",
            byline = byline ?: "",
            bookTitle = bookTitle ?: "",
            bookAuthor = bookAuthor ?: "",
            summary = summary ?: ""
        )
    } else {
        null
    }
}
