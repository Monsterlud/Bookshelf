package com.monsalud.bestsellerbookshelf

import android.app.Application
import com.monsalud.bestsellerbookshelf.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class BookshelfApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@BookshelfApplication)
            modules(appModule)
        }
    }
}