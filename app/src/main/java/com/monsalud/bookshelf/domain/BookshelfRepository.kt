package com.monsalud.bookshelf.domain

interface BookshelfRepository {

    suspend fun getAllBookList() : Result<String?>

    suspend fun getSpecificBookList(listName: String) : Result<String?>

    suspend fun getBookReview(isbn: String) : Result<String?>
}