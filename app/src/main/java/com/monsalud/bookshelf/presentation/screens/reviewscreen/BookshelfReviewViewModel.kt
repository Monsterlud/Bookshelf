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

            repository.getBookReview(isbn13).collect { result ->
                result.onSuccess {
                    _bookReviewState.value = if (it != null) {
                        BookReviewState.Success(it)
                    } else {
                        BookReviewState.NoReview
                    }
                }.onFailure {
                    _bookReviewState.value = BookReviewState.Error
                }
            }
        }
    }
}

sealed class BookReviewState {
    data object Initial : BookReviewState()
    data object Loading : BookReviewState()
    data class Success(val review: BookReviewEntity) : BookReviewState()
    data object NoReview : BookReviewState()
    data object Error : BookReviewState()
}
