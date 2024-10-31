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
import com.monsalud.bookshelf.domain.BookshelfRepository
import com.monsalud.bookshelf.presentation.screens.listscreen.BookshelfListViewModel
import com.monsalud.bookshelf.presentation.screens.reviewscreen.BookshelfReviewViewModel
import com.monsalud.bookshelf.work.BookshelfWorkerFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    val moduleInstance = AppModule()

    fun provideBookListDao(database: BookshelfDatabase) = database.bookListDao()
    fun provideBookReviewDao(database: BookshelfDatabase) = database.bookReviewDao()

    single { BookshelfDatabase.getDatabase(androidApplication()) } bind BookshelfDatabase::class
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

    viewModel { BookshelfListViewModel(repository = get()) }
    viewModel { BookshelfReviewViewModel(repository = get()) }

    single {
        BookshelfRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
            moshi = get(),
        )
    } bind BookshelfRepository::class

    single {
        LocalDataSourceImpl(
            bookListDao = get(),
            bookReviewDAO = get(),
            dataStore = get(),
        )
    } bind LocalDataSource::class
    single { RemoteDataSourceImpl(client = get()) } bind RemoteDataSource::class
    single(qualifier = null) { moduleInstance.ktorClient() }

    single { BookshelfDataStore(context = get()) }
    single { BookshelfWorkerFactory(repository = get()) }
    single { Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    }
}

class AppModule {

    fun ktorClient() = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    explicitNulls = false
                }
            )
        }
        engine {
            connectTimeout = TIMEOUT
            socketTimeout = TIMEOUT
        }
    }

    companion object {
        const val TIMEOUT = 10_000
    }
}
