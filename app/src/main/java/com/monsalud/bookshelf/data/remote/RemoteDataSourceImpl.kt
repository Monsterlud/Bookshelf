package com.monsalud.bookshelf.data.remote

import com.monsalud.bookshelf.data.RemoteDataSource
import com.monsalud.bookshelf.data.utils.DataError
import com.monsalud.bookshelf.data.utils.NetworkConstants
import com.monsalud.bookshelf.data.utils.NetworkConstants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import timber.log.Timber


class RemoteDataSourceImpl(
    private val client: HttpClient
) : RemoteDataSource {

    override suspend fun getBooksInListFromApi(list: String): Result<String?> {
        return try {
            val specificBookList =
                client.get<String>("${BASE_URL}/lists/${list}.json") {
                    contentType(Json)
                    parameter("api-key", NetworkConstants.API_KEY)
                }
            Result.success(specificBookList)
        } catch (e: IOException) {
            Result.failure(DataError.Network(e))
        } catch (e: Exception) {
            Result.failure(DataError.Unknown(e))
        }
    }

    override suspend fun getBookReviewFromApi(isbn: String): Result<String?> {
        return try {
            val bookReview =
                client.get<String>("${BASE_URL}/reviews.json") {
                    contentType(Json)
                    parameter("api-key", NetworkConstants.API_KEY)
                    parameter("isbn", isbn)
                }
            Timber.d("Successfully retrieved from API: $bookReview")
            Result.success(bookReview)
        } catch (e: IOException) {
            Result.failure(DataError.Network(e))
        } catch (e: Exception) {
            Result.failure(DataError.Unknown(e))
        }
    }
}
