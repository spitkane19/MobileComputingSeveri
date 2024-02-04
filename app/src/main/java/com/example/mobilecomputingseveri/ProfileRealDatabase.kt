package com.example.mobilecomputingseveri

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@Singleton
@Database(entities = [(ProfileDatabase::class)], version = 1)
abstract class YourRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: YourRoomDatabase? = null


        fun getDatabase(context: Context): YourRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        YourRoomDatabase::class.java,
                        "profile_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}