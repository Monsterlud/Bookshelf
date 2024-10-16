package com.monsalud.bookshelf.di

import android.util.Log
import com.monsalud.bookshelf.data.BookshelfRepositoryImpl
import com.monsalud.bookshelf.data.LocalDataSource
import com.monsalud.bookshelf.data.RemoteDataSource
import com.monsalud.bookshelf.data.local.LocalDataSourceImpl
import com.monsalud.bookshelf.data.remote.RemoteDataSourceImpl
import com.monsalud.bookshelf.domain.BookshelfRepository
import com.monsalud.bookshelf.presentation.BookshelfViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.observer.ResponseObserver
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    val moduleInstance = AppModule()

    viewModel { BookshelfViewModel(repository = get()) }
    single { BookshelfRepositoryImpl(localDataSource = get(), remoteDataSource = get()) } bind BookshelfRepository::class
    single { LocalDataSourceImpl() } bind LocalDataSource::class
    single { RemoteDataSourceImpl(client = get()) } bind RemoteDataSource::class
    single(qualifier = null) { moduleInstance.ktorClient() }
    single { BookshelfDataStore(get()) }
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
            connectTimeout = 10_000
            socketTimeout = 10_000
        }
    }
}