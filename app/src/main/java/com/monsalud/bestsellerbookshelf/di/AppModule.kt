package com.monsalud.bestsellerbookshelf.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.koin.dsl.module

val appModule = module {
    val moduleInstance = AppModule()
}

class AppModule {

}