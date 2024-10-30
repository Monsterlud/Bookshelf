package com.monsalud.bookshelf

import android.app.Application
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.monsalud.bookshelf.di.appModule
import com.monsalud.bookshelf.work.BookshelfWorkerFactory
import com.monsalud.bookshelf.work.RefreshBooksDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BookshelfApplication() : Application(), Configuration.Provider, KoinComponent {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    private var _workManagerConfiguration: Configuration? = null
    override val workManagerConfiguration: Configuration
        get() = _workManagerConfiguration ?:
        Configuration.Builder()
            .setWorkerFactory(get<BookshelfWorkerFactory>())
            .build().also { _workManagerConfiguration = it }


    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@BookshelfApplication)
            modules(appModule)
        }

        val workerFactory = get<BookshelfWorkerFactory>()
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(this, config)

        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(true)
            .build()

        val repeatingTask = PeriodicWorkRequestBuilder<RefreshBooksDataWorker>(
            repeatInterval = 1,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()

        WorkManager.getInstance(this@BookshelfApplication)
            .enqueueUniquePeriodicWork(
                RefreshBooksDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingTask
            )
    }
}
