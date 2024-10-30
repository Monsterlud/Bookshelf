package com.monsalud.bookshelf.presentation.screens.reviewscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookReviewEntity
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import com.monsalud.bookshelf.domain.BookshelfRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class BookshelfReviewViewModel(
    private val repository: BookshelfRepository
) : ViewModel() {

    private val _bookReviewState = MutableStateFlow<BookReviewState>(BookReviewState.Initial)
    val bookReviewState: StateFlow<BookReviewState> = _bookReviewState.asStateFlow()

    fun fetchBookReview(isbn13: String) {
        viewModelScope.launch {
            Timber.d("Fetching book review for ISBN13: $isbn13")
            _bookReviewState.value = BookReviewState.Loading
            try {
                repository.getBookReview(isbn13).collect { review ->
                    _bookReviewState.value = if (review != null) {
                        BookReviewState.Success(review)
                    } else {
                        BookReviewState.NoReview
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching book review")
                _bookReviewState.value =
                    BookReviewState.Error(e.message ?: "Error fetching book review")
            }
        }
    }
}

sealed class BookReviewState {
    data object Initial : BookReviewState()
    data object Loading : BookReviewState()
    data class Success(val review: BookReviewEntity) : BookReviewState()
    data object NoReview : BookReviewState()
    data class Error(val message: String) : BookReviewState()
}
