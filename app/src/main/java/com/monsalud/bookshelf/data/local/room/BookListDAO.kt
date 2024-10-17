package com.monsalud.bookshelf.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BookListDAO {

    /** DAO methods for Book List */

    // Book list table operations
    @Transaction
    @Query(
        """
            SELECT *
            FROM book_list_table blt
            INNER JOIN books_table bt ON blt.listName = bt.listName
            WHERE blt.listName = :listName
        """
    )
    fun getListWithBooks(listName: String): Flow<ListWithBooks?>

    @Query("SELECT * FROM books_table WHERE listName = :listName")
    fun getBooksForList(listName: String): Flow<List<BookEntity>>

    @Transaction
    suspend fun deleteListWithBooks(listName: String) {
        deleteBookList(listName)
        deleteBooksForList(listName)
    }

    @Transaction
    suspend fun insertListWithBooks(listWithBooks: ListWithBooks) {
            insertBookList(listWithBooks.bookList)
            insertBooks(listWithBooks.books)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookList(bookList: BookListEntity)

    @Query("DELETE FROM book_list_table WHERE listName = :listName")
    suspend fun deleteBookList(listName: String)

    @Query("SELECT * FROM book_list_table WHERE listName = :listName")
    fun getBookList(listName: String): Flow<BookListEntity?>

    @Query("SELECT * FROM book_list_table")
    fun getAllBookLists(): Flow<List<BookListEntity>>


    // Books table operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("DELETE FROM books_table WHERE primaryIsbn13 = :isbn13")
    suspend fun deleteBook(isbn13: String)

    @Query("DELETE FROM books_table WHERE listName = :listName")
    suspend fun deleteBooksForList(listName: String)

    @Query("SELECT * FROM books_table WHERE primaryIsbn13 = :isbn13")
    fun getBook(isbn13: String): Flow<BookEntity?>

    @Query("SELECT * FROM books_table")
    fun getAllBooks(): Flow<List<BookEntity>>


    // Debugging Tools

    @Query("SELECT COUNT(*) FROM book_list_table WHERE listName = :listName")
    fun checkListExists(listName: String): Int

    @Query("SELECT COUNT(*) FROM books_table WHERE listName = :listName")
    fun countBooksInList(listName: String): Int

    @Query("SELECT COUNT(*) FROM book_reviews_table WHERE primaryIsbn13 = :isbn13")
    fun countReviewsForBook(isbn13: String): Int
}
