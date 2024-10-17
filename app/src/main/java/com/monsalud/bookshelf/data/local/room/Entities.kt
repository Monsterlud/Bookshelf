package com.monsalud.bookshelf.data.local.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

/** Database Entities for Books by List */

@Entity(tableName = "book_list_table")
data class BookListEntity(
    @PrimaryKey val listName: String,
    val bestsellersDate: String,
    val publishedDate: String,
    val displayName: String,
    val normalListEndsAt: Int,
    val updated: String,
)

@Entity(
    tableName = "books_table",
    foreignKeys = [
        ForeignKey(
            entity = BookListEntity::class,
            parentColumns = ["listName"],
            childColumns = ["listName"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("listName")]
)
data class BookEntity(
    @PrimaryKey val primaryIsbn13: String,
    val listName: String, // This is a foreign key to BookListEntity
    val rank: Int,
    val rankLastWeek: Int,
    val weeksOnList: Int,
    val asterisk: Int,
    val dagger: Int,
    val primaryIsbn10: String,
    val publisher: String,
    val description: String,
    val price: Int,
    val title: String,
    val author: String,
    val contributor: String,
    val contributorNote: String,
    val bookImage: String,
    val amazonProductUrl: String,
    val ageGroup: String,
    val bookReviewLink: String,
    val firstChapterLink: String,
    val sundayReviewLink: String,
    val articleChapterLink: String,
)

data class ListWithBooks(
    @Embedded val bookList: BookListEntity,
    @Relation(
        parentColumn = "listName",
        entityColumn = "listName"
    )
    val books: List<BookEntity>
)


/** Database Entities for Book Reviews */

@Entity(tableName = "book_reviews_table")
data class BookReviewEntity(
    @PrimaryKey val bookIsbn13: String,
    val url: String,
    val publicationDt: String,
    val byline: String,
    val bookTitle: String,
    val bookAuthor: String,
    val summary: String,
)
