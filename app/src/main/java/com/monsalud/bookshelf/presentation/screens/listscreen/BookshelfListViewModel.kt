package com.monsalud.bookshelf.presentation.screens.listscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monsalud.bookshelf.data.local.room.ListWithBooks
import com.monsalud.bookshelf.domain.BookshelfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class BookshelfListViewModel(
    private val repository: BookshelfRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookListState>(BookListState.Loading)
    val uiState: StateFlow<BookListState> = _uiState.asStateFlow()

    private val _showOnboarding = MutableStateFlow(true)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUserPreferencesFlow().collect { userPreferences ->
                _showOnboarding.update { !userPreferences.hasSeenOnboardingDialog }
            }
        }
    }

    fun syncAndObserveBookList(newListName: String, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                if (forceRefresh) {
                    val currentList = (uiState.value as? BookListState.Success)?.bookList
                    _uiState.update { BookListState.Refreshing(books = currentList) }
                }

                // Start observing the local database for changes
                val localDbJob = launch {
                    repository.getListWithBooks(newListName)
                        .collect { listWithBooks ->
                            if (listWithBooks != null) {
                                _uiState.update { BookListState.Success(bookList = listWithBooks) }
                            }
                        }
                }

                // Refresh the API if needed
                if (forceRefresh || !repository.hasDataForList(newListName)) {
                    if (!forceRefresh) {
                        _uiState.update { BookListState.Loading }
                    }

                    repository.refreshBookListInDbFromApi(newListName)
                        .fold(
                            onSuccess = {
                                Timber.d("Successfully refreshed $newListName list from API")
                            },
                            onFailure = { error ->
                                Timber.e(error, "Failed to refresh $newListName list from API")
                                _uiState.update {
                                    when {
                                        error is IOException -> BookListState.Error(
                                            message = "Please check your internet connection and try again"
                                        )

                                        error.localizedMessage?.contains("429") == true -> BookListState.Error(
                                            message = "Too many requests. Please try again later"
                                        )

                                        else -> BookListState.Error(
                                            message = "An unexpected error occurred"
                                        )
                                    }
                                }
                                // Cancel the local database job if the API call fails
                                if (!repository.hasDataForList(newListName)) {
                                    localDbJob.cancel()
                                }
                            }
                        )
                }
            } catch (e: Exception) {
                _uiState.update {
                    BookListState.Error(
                        message = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        viewModelScope.launch {
            repository.updateHasSeenOnboardingDialog(hasSeen)
            _showOnboarding.update { !hasSeen }
        }
    }
}


sealed class BookListState {
    data class Success(
        val bookList: ListWithBooks? = null,
    ) : BookListState()

    data object Loading : BookListState()

    data class Refreshing(
        val books: ListWithBooks? = null,
    ) : BookListState()

    data class Error(
        val message: String,
    ) : BookListState()
}
