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

class BookshelfListViewModel(
    private val repository: BookshelfRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookListState>(BookListState.Loading)
    val uiState: StateFlow<BookListState> = _uiState.asStateFlow()

    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUserPreferencesFlow().collect { userPreferences ->
                val hasSeenOnboardingDialog = userPreferences.hasSeenOnboardingDialog
                _showOnboarding.update { hasSeenOnboardingDialog }
            }
        }
    }

    fun syncAndObserveBookList(newListName: String, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (forceRefresh) {
                val currentList = (uiState.value as? BookListState.Success)?.bookList
                _uiState.update { BookListState.Refreshing(books = currentList) }
            } else if (!repository.hasDataForList(newListName)) {
                _uiState.update { BookListState.Loading }
            }
            repository.refreshBookListInDbFromApi(newListName)

            repository.getListWithBooks(newListName)
                .collect { listWithBooks ->
                    _uiState.update { BookListState.Success(bookList = listWithBooks) }
                }
        }
    }

    fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        viewModelScope.launch {
            repository.updateHasSeenOnboardingDialog(hasSeen)
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
