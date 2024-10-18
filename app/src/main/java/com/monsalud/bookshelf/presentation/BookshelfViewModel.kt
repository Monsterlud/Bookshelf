package com.monsalud.bookshelf.presentation

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class BookshelfViewModel(
    private val repository: BookshelfRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isLoadingPreferences = MutableStateFlow(true)
    val isLoadingPreferences: StateFlow<Boolean> = _isLoadingPreferences.asStateFlow()

    private val _bookReviewState = MutableStateFlow<BookReviewState>(BookReviewState.Initial)
    val bookReviewState: StateFlow<BookReviewState> = _bookReviewState.asStateFlow()

    private val _listName = MutableStateFlow<String?>(null)

    fun setListName(listName: String) {
        _listName.value = listName
        refreshBookList(listName)
    }

    private fun refreshBookList(listName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.refreshBookListInDBFromAPI(listName)
            } catch (e: Exception) {
                Timber.e(e, "Error refreshing book list")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchBookReview(isbn13: String) {
        viewModelScope.launch {
            Timber.d("Fetching book review for ISBN13: $isbn13")
            _bookReviewState.value = BookReviewState.Loading
            try {
                repository.getBookReview(isbn13).collect { review ->
                    _bookReviewState.value = if (review!= null) {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookListFlow: StateFlow<ListWithBooks?> = _listName
        .filterNotNull()
        .flatMapLatest { listName ->
            repository.getListWithBooks(listName)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(10_000),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val userPreferencesFlow: StateFlow<BookshelfDataStore.UserPreferences> = flow {
        emit(repository.getUserPreferencesFlow())
    }.onEach { _isLoadingPreferences.value = false }
        .flatMapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = BookshelfDataStore.UserPreferences(
                hasSeenOnboardingDialog = true,
            )
        )

    fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        viewModelScope.launch {
            repository.updateHasSeenOnboardingDialog(hasSeen)
        }
    }
}

sealed class BookReviewState {
    object Initial : BookReviewState()
    object Loading : BookReviewState()
    data class Success(val review: BookReviewEntity) : BookReviewState()
    object NoReview : BookReviewState()
    data class Error(val message: String) : BookReviewState()
}
