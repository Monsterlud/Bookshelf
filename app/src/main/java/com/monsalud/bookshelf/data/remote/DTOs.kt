package com.monsalud.bookshelf.data.remote


/** DTOs to handle Full Overview Best Sellers API data */

data class AllListDTO(
    val status: String,
    val copyright: String,
    val num_results: Int,
    val results: List<AllListResultDTO>
)

data class AllListResultDTO(
    val bestsellers_date: String,
    val published_date: String,
    val lists: List<AllListListDTO>
)
data class AllListListDTO(
    val list_id: Int,
    val list_name: String,
    val display_name: String,
    val updated: String,
    val list_image: String,
    val books: List<AllListBookDTO>
)

data class AllListBookDTO(
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
)


/** DTOs to handle Single List Best Sellers API data */

data class SingleListDTO(
    val status: String,
    val copyright: String,
    val num_results: Int,
    val last_modified: String,
    val results: List<SingleListResultDTO>
)

data class SingleListResultDTO(
    val list_name: String,
    val bestsellers_date: String,
    val published_date: String,
    val display_name: String,
    val normal_list_ends_at: Int,
    val updated: String,
    val books: List<SingleListBookDTO>
)

data class SingleListBookDTO(
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
    val isbns: List<IsbnDTO>
)

data class IsbnDTO(
    val isbn10: String,
    val isbn13: String
)


/** DTOs to handle Book Reviews API data */

data class BookReviewsDTO(
    val status: String,
    val copyright: String,
    val num_results: Int,
    val results: List<BookReviewResultDTO>
)

data class BookReviewResultDTO(
    val url: String,
    val publication_dt: String,
    val byline: String,
    val book_title: String,
    val book_author: String,
    val summary: String,
    val isbn13: List<String>,
)

