package com.monsalud.bookshelf.data

interface RemoteDataSource {
    suspend fun getAllBookListFromApi() : Result<String?>
    suspend fun getSpecificBookListFromApi(list: String) : Result<String?>
    suspend fun getBookReviewFromApi(isbn: String) : Result<String?>
}
