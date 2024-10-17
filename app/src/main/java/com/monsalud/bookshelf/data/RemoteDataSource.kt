package com.monsalud.bookshelf.data

interface RemoteDataSource {
    suspend fun getBooksInListFromApi(list: String): Result<String?>
    suspend fun getBookReviewFromApi(isbn: String): Result<String?>
}
