package com.monsalud.bookshelf.data.remote

import com.monsalud.bookshelf.data.RemoteDataSource
import com.monsalud.bookshelf.data.remote.booklistapi.BookListErrorResponse
import com.monsalud.bookshelf.data.remote.booklistapi.BookListResponseDto
import com.monsalud.bookshelf.data.remote.bookreviewapi.BookReviewDto
import com.monsalud.bookshelf.data.remote.bookreviewapi.BookReviewErrorResponse
import com.monsalud.bookshelf.data.remote.bookreviewapi.BookReviewResponseDto
import com.monsalud.bookshelf.data.utils.DataError
import com.monsalud.bookshelf.data.utils.NetworkConstants
import com.monsalud.bookshelf.data.utils.NetworkConstants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import timber.log.Timber


class RemoteDataSourceImpl(
    private val client: HttpClient
) : RemoteDataSource {

    override suspend fun getBooksInListFromApi(list: String): Result<BookListResponseDto> {
        var result: Result<BookListResponseDto> = Result.failure(DataError.Unknown(Exception()))
        runCatching {
            val response =
                client.get("${BASE_URL}/lists/${list}.json") {
                    contentType(Json)
                    parameter("api-key", NetworkConstants.API_KEY)
                }
            val body = response.bodyAsText()
            Timber.d("Response body for fetched book list: $body")
            Timber.d("Response status for fetched book list: ${response.status.value}")
            result = when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(response.body<BookListResponseDto>())
                }

                else -> {
                    val error = response.body<BookListErrorResponse>()
                    Result.failure(
                        Exception(
                            error.fault?.faultString ?: response.status.description
                        )
                    )
                }
            }
        }.onFailure {
            Timber.e("Error fetching book list: ${it.message}")
            result = Result.failure(it)
        }
        return result
    }

    override suspend fun getBookReviewFromApi(isbn: String): Result<BookReviewResponseDto> {
        var result: Result<BookReviewResponseDto> =
            Result.failure(DataError.Unknown(Exception()))
        runCatching {
            val response = client.get("${BASE_URL}/reviews.json") {
                contentType(Json)
                parameter("api-key", NetworkConstants.API_KEY)
                parameter("isbn", isbn)
            }
            val body = response.bodyAsText()
            Timber.d("Response body for fetched book review: $body")
            Timber.d("Response status for fetched book review: ${response.status.value}")
            result = when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(response.body<BookReviewResponseDto>())
                }

                else -> {
                    val error = response.body<BookReviewErrorResponse>()
                    Result.failure(
                        Exception(
                            error.fault?.faultString ?: response.status.description
                        )
                    )
                }
            }
        }.onFailure {
            Timber.e("Error fetching book review: ${it.message}")
            result = Result.failure(it)
        }
        return result
    }
}
