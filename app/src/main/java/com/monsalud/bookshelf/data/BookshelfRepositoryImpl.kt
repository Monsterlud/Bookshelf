package com.monsalud.bookshelf.data

import com.monsalud.bookshelf.domain.BookshelfRepository

class BookshelfRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : BookshelfRepository {
    override suspend fun getAllBookList(): Result<String?> {
        return remoteDataSource.getAllBookListFromApi()
    }

    override suspend fun getSpecificBookList(listName: String): Result<String?> {
        return remoteDataSource.getSpecificBookListFromApi(list = listName)
    }

    override suspend fun getBookReview(isbn: String): Result<String?> {
        return remoteDataSource.getBookReviewFromApi(isbn = isbn)
    }
}