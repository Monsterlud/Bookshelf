package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.data.remote.booklistapi.BookListResponseDto
import com.monsalud.bookshelf.data.remote.bookreviewapi.BookReviewResponseDto

interface RemoteDataSource {

    /** Remote DataSource Operations */

    /**
     * Fetches the book list with the given name from the remote API.
     * @param list The name of the book list to fetch.
     * @return A [Result] object containing the [BookListResponseDto] if the API call is successful,
     * or a [Failure] object with the corresponding error if the API call fails.
     */
    suspend fun getBooksInListFromApi(list: String): Result<BookListResponseDto>

    /**
     * Fetches the book review for the given ISBN from the remote API.
     * @param isbn The ISBN of the book to fetch the review for.
     * @return A [Result] object containing the [BookReviewResponseDto] if the API call is successful,
     * or a [Failure] object with the corresponding error if the API call fails.
     */
    suspend fun getBookReviewFromApi(isbn: String): Result<BookReviewResponseDto>
}
