package com.monsalud.bookshelf.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import timber.log.Timber

const val DATABASE_NAME = "bookshelf_database"

@Database(
    entities = [BookListEntity::class, BookEntity::class, BookReviewEntity::class,],
    version = 1,
    exportSchema = false
)
abstract class BookshelfDatabase : RoomDatabase() {

    abstract fun bookListDao(): BookListDAO
    abstract fun bookReviewDao(): BookReviewDAO

    companion object {
        @Volatile
        private var INSTANCE: BookshelfDatabase? = null

        fun getDatabase(context: Context): BookshelfDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookshelfDatabase::class.java,
                    DATABASE_NAME,
                ).build()
                INSTANCE = instance
                Timber.d("Created database at: ${instance.openHelper.writableDatabase.path}")
                return instance
            }
        }
    }
}