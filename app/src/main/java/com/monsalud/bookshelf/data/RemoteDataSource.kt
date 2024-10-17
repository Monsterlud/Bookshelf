package com.monsalud.bookshelf.data

interface RemoteDataSource {

    /** Remote DataSource Operations */

    /**
     * Retrieves books in a specified list from the remote API.
     *
     * @param list The name or identifier of the list to fetch books from.
     * @return A Result object containing a String representation of the API response
     *         (likely JSON) if successful, or an error if the API call fails.
     *         The String can be null if the API returns no data.
     */
    suspend fun getBooksInListFromApi(list: String): Result<String?>

    /**
     * Fetches a book review from the remote API for a specific book.
     *
     * @param isbn The ISBN (International Standard Book Number) of the book to get the review for.
     * @return A Result object containing a String representation of the API response
     *         (likely JSON) if successful, or an error if the API call fails.
     *         The String can be null if no review is found for the given ISBN.
     */
    suspend fun getBookReviewFromApi(isbn: String): Result<String?>
}
