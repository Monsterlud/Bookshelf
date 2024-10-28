package com.monsalud.bookshelf.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.monsalud.bookshelf.data.BookshelfRepositoryImpl
import com.monsalud.bookshelf.data.local.LocalDataSourceImpl
import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookshelfDatabase
import com.monsalud.bookshelf.data.remote.RemoteDataSourceImpl
import com.monsalud.bookshelf.domain.BookshelfRepository
import io.ktor.client.HttpClient

class RefreshBooksDataWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: BookshelfRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        return try {
            repository.refreshBookListInDBFromAPI("Hardcover Fiction")
            repository.refreshBookListInDBFromAPI("Hardcover Nonfiction")
            repository.refreshBookListInDBFromAPI("Culture")
            repository.refreshBookListInDBFromAPI("Food and Fitness")
            repository.refreshBookListInDBFromAPI("Graphic Books and Manga")
            repository.refreshBookListInDBFromAPI("Hardcover Political Books")
            repository.refreshBookListInDBFromAPI("Science")
            repository.refreshBookListInDBFromAPI("Sports")
            repository.refreshBookListInDBFromAPI("Travel")
            return Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshBooksDataWorker"
    }
}