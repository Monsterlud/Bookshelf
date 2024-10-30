package com.monsalud.bookshelf.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookReviewDao {

    /** DAO methods for Book Reviews */

    // Book reviews table operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookReview(bookReview: BookReviewEntity)

    @Query("DELETE FROM book_reviews_table WHERE bookIsbn13 = :isbn13")
    suspend fun deleteBookReview(isbn13: String)

    @Query("SELECT * FROM book_reviews_table WHERE bookIsbn13 = :isbn13")
    fun getBookReview(isbn13: String): Flow<BookReviewEntity?>
}