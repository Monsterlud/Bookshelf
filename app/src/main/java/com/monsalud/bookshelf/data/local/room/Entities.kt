package com.monsalud.bookshelf.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_lists")
data class AllListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val status: String,
    val copyright: String,
    val num_results: Int
)

@Entity(tableName = "all_list_results")
data class AllListResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bestsellers_date: String,
    val published_date: String,
    val allListId: Int, // Foreign key to link with AllListEntity
)

@Entity(tableName = "all_list_lists")
data class AllListListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val list_id: Int,
    val list_name: String,
    val display_name: String,
    val updated: String,
    val list_image: String,
    val allListResultId: Int, // Foreign key to link with AllListResultEntity
)

@Entity(tableName = "all_books")
data class AllListBookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val age_group: String,
    val author: String,
    val contributer: String,
    val contributor_note: String,
    val created_date: String,
    val description: String,
    val price: Int,
    val primary_isbn13: String,
    val primary_isbn10: String,
    val publisher: String,
    val rank: Int,
    val title: String,
    val updated_date: String,
    val allListListId: Int, // Foreign key to link with AllListListEntity
)


@Entity(tableName = "single_lists")
data class SingleListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val status: String,
    val copyright: String,
    val num_results: Int,
    val last_modified: String
)

@Entity(tableName = "single_list_results")
data class SingleListResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val list_name: String,
    val bestsellers_date: String,
    val published_date: String,
    val display_name: String,
    val normal_list_ends_at: Int,
    val updated: String,
    val singleListId: Int, // Foreign key to link with SingleListEntity
)

@Entity(tableName = "single_list_books")
data class SingleListBookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rank: Int,
    val rank_last_week: Int,
    val weeks_on_list: Int,
    val asterisk: Int,
    val dagger: Int,
    val primary_isbn10: String,
    val primary_isbn13: String,
    val publisher: String,
    val description: String,
    val price: Int,
    val title: String,
    val author: String,
    val contributor: String,
    val contributor_note: String,
    val book_image: String,
    val amazon_product_url: String,
    val age_group: String,
    val book_review_link: String,
    val first_chapter_link: String,
    val singleListResultId: Int, // Foreign key to link with SingleListResultEntity
)

@Entity(tableName = "isbns")
data class IsbnEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val isbn10: String,
    val isbn13: String,
    val singleListBookId: Int // Foreign key to link with SingleListBookEntity
)

@Entity(tableName = "book_reviews")
data class BookReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val publication_dt: String,
    val byline: String,
    val book_title: String,
    val book_author: String,
    val summary: String,
    val isbn13: String // Store as comma-separated string
)

