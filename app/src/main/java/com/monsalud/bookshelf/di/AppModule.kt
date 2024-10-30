package com.monsalud.bookshelf.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.monsalud.bookshelf.data.BookshelfRepositoryImpl
import com.monsalud.bookshelf.data.LocalDataSource
import com.monsalud.bookshelf.data.RemoteDataSource
import com.monsalud.bookshelf.data.local.LocalDataSourceImpl
import com.monsalud.bookshelf.data.local.datastore.BookshelfDataStore
import com.monsalud.bookshelf.data.local.room.BookshelfDatabase
import com.monsalud.bookshelf.data.remote.RemoteDataSourceImpl
import com.monsalud.bookshelf.data.utils.NetworkUtils
import com.monsalud.bookshelf.domain.BookshelfRepository
import com.monsalud.bookshelf.presentation.BookshelfViewModel
import com.monsalud.bookshelf.work.BookshelfWorkerFactory
import com.monsalud.bookshelf.work.RefreshBooksDataWorker
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.observer.ResponseObserver
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    val moduleInstance = AppModule()

    fun provideDatabase(application: Application): BookshelfDatabase {
        return Room.databaseBuilder(
            application,
            BookshelfDatabase::class.java,
            AppModule.BOOKSHELF_DATABASE
        ).build()
    }

    fun provideBookListDao(database: BookshelfDatabase) = database.bookListDao()
    fun provideBookReviewDao(database: BookshelfDatabase) = database.bookReviewDao()

    single { provideDatabase(androidApplication()) } bind BookshelfDatabase::class
    single {
        provideBookListDao(
            database = get()
        )
    }
    single {
        provideBookReviewDao(
            database = get()
        )
    }

    viewModel { BookshelfViewModel(repository = get()) }
    single {
        BookshelfRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get()
        )
    } bind BookshelfRepository::class
    single {
        LocalDataSourceImpl(
            bookListDao = get(),
            bookReviewDAO = get(),
            dataStore = get()
        )
    } bind LocalDataSource::class
    single { RemoteDataSourceImpl(client = get()) } bind RemoteDataSource::class
    single(qualifier = null) { moduleInstance.ktorClient() }

    single { BookshelfDataStore(context = get()) }
    single { NetworkUtils() }
    single { BookshelfWorkerFactory(repository = get()) }
}

class AppModule {

    fun ktorClient() = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }
        install(ResponseObserver) {
            onResponse { response ->
                Log.i("KOIN", "${response.status.value}")
            }
        }
        engine {
            connectTimeout = TIMEOUT
            socketTimeout = TIMEOUT
        }
    }

    companion object {
        const val BOOKSHELF_DATABASE = "bookshelf_database"
        const val TIMEOUT = 10_000
    }
}
