package com.monsalud.bookshelf.presentation.screens.listscreen

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

class BookshelfListViewModel(
    private val repository: BookshelfRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isLoadingPreferences = MutableStateFlow(true)
    val isLoadingPreferences: StateFlow<Boolean> = _isLoadingPreferences.asStateFlow()

    private val _bookListFlow = MutableStateFlow<ListWithBooks?>(null)
    val bookListFlow = _bookListFlow.asStateFlow()

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

    init {
        viewModelScope.launch {
            _isLoading.value = false
        }
    }

    fun setListName(newListName: String) {
        viewModelScope.launch {
            if (!repository.hasDataForList(newListName)) {
                refreshBookList(newListName)
            }
            repository.getListWithBooks(newListName)
                .collect { listWithBooks ->
                    _bookListFlow.value = listWithBooks
                }
        }
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

    fun updateHasSeenOnboardingDialog(hasSeen: Boolean) {
        viewModelScope.launch {
            repository.updateHasSeenOnboardingDialog(hasSeen)
        }
    }
}
