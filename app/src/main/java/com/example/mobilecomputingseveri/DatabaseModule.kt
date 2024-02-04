package com.example.mobilecomputingseveri

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideYourRoomDatabase(application: Application): YourRoomDatabase {
        return Room.databaseBuilder(
            application,
            YourRoomDatabase::class.java,
            "profile_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: YourRoomDatabase): UserDao {
        return database.userDao()
    }
}