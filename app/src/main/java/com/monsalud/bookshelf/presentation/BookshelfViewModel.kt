package com.monsalud.bookshelf.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monsalud.bookshelf.domain.BookshelfRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class BookshelfViewModel(
    private val repository: BookshelfRepository
) : ViewModel() {

    suspend fun getAllBooks() {
        viewModelScope.launch {
            val result = repository.getAllBookList()
            when {
                result.isSuccess -> {
                    val allBooksString = result.getOrNull()
                    if (allBooksString != null) {
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
}
