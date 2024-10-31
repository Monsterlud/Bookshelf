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
            repository.refreshBookListInDbFromApi("Hardcover Fiction")
            repository.refreshBookListInDbFromApi("Hardcover Nonfiction")
            repository.refreshBookListInDbFromApi("Culture")
            repository.refreshBookListInDbFromApi("Food and Fitness")
            repository.refreshBookListInDbFromApi("Graphic Books and Manga")
            repository.refreshBookListInDbFromApi("Hardcover Political Books")
            repository.refreshBookListInDbFromApi("Science")
            repository.refreshBookListInDbFromApi("Sports")
            repository.refreshBookListInDbFromApi("Travel")
            return Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshBooksDataWorker"
    }
}