package com.uma.newsapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Author     : Umapathi
 * Email      : umapathir2@gmail.com
 * Github     : https://github.com/umapathi2128
 * Created on : 2021-06-06.
 */
@Database(
    entities = [DataEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun DataDAO(): DataDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "data-list.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}