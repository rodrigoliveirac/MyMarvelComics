package com.rodcollab.mymarvelcomics.core.database.di

import android.app.Application
import com.rodcollab.mymarvelcomics.core.database.AppDatabase
import com.rodcollab.mymarvelcomics.core.database.dao.FavoriteComicsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppDatabaseModule {
    @Singleton
    @Provides
    fun providesAppDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun providesFavoriteComicsDao(database: AppDatabase): FavoriteComicsDao {
        return database.favoriteComicsDao()
    }
}