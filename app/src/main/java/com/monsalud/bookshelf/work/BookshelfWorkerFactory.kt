package com.monsalud.bookshelf.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.monsalud.bookshelf.domain.BookshelfRepository

class BookshelfWorkerFactory(
    private val repository: BookshelfRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            RefreshBooksDataWorker::class.java.name -> {
                RefreshBooksDataWorker(appContext, workerParameters, repository)
            }
            else -> null
        }
    }
}