package com.monsalud.bookshelf.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monsalud.bookshelf.domain.BookshelfRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class BookshelfViewModel(
    private val repository: BookshelfRepository
) : ViewModel() {

    private val _isLoadingPreferences = MutableStateFlow(true)
    val isLoadingPreferences: StateFlow<Boolean> = _isLoadingPreferences.asStateFlow()

        viewModelScope.launch {
            val result = repository.getAllBookList()
            when {
                result.isSuccess -> {
                    val allBooksString = result.getOrNull()
    @OptIn(ExperimentalCoroutinesApi::class)
                        Timber.d("Success: $allBooksString")
                    } else {
                        Timber.d("allBooksString is null")
                    }
                }
                result.isFailure -> {
                    Timber.d("Failure: ${result.exceptionOrNull()}")
                }
            }
        }
    }
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
}
