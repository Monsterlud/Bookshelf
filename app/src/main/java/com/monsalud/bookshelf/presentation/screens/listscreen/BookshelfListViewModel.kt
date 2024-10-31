package com.monsalud.bookshelf.presentation.screens.listscreen

import androidx.datastore.preferences.protobuf.ListValue
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
import kotlinx.coroutines.flow.combine
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
    private val _isLoadingPreferences = MutableStateFlow(true)
    private val _bookListFlow = MutableStateFlow<ListWithBooks?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _userPreferencesFlow: StateFlow<BookshelfDataStore.UserPreferences> = flow {
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

    val uiState: StateFlow<BookListState> = combine(
        _bookListFlow,
        _isLoading,
        _isLoadingPreferences,
        _userPreferencesFlow,
    ) { bookList, isLoading, isLoadingPreferences, userPreferences ->
        if (isLoading) {
            BookListState.Loading
        } else {
            BookListState.Success(
                bookList = bookList,
                isLoading = isLoading,
                isLoadingPreferences = isLoadingPreferences,
                userPreferences = userPreferences,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = BookListState.Loading
    )

    init {
        viewModelScope.launch {
            _isLoading.value = false
        }
    }

    fun setListName(newListName: String) {
        viewModelScope.launch {
            Timber.d("setListName called with listName: $newListName")
            if (!repository.hasDataForList(newListName)) {
                Timber.d("hasDataForList returned false, refreshing book list")
                refreshBookList(newListName)
            }
            repository.getListWithBooks(newListName)
                .collect { listWithBooks ->
                    Timber.d("Received listWithBooks from repository: $listWithBooks")
                    _bookListFlow.value = listWithBooks
                }
        }
    }

    private fun refreshBookList(listName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.refreshBookListInDbFromApi(listName)
            _isLoading.value = false
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
        val bookList: ListWithBooks?,
        val isLoading: Boolean,
        val isLoadingPreferences: Boolean,
        val userPreferences: BookshelfDataStore.UserPreferences,
    ) : BookListState()

    data object Loading : BookListState()
    data object Empty : BookListState()
    data class Error(
        val message: String,
        val bookList: ListWithBooks? = null,
        ) : BookListState()

}
