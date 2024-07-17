package com.rodcollab.mymarvelcomics.core.database.di

import android.app.Application
import com.rodcollab.mymarvelcomics.core.database.AppDatabase
import com.rodcollab.mymarvelcomics.core.database.DefaultTransactionProvider
import com.rodcollab.mymarvelcomics.core.database.TransactionProvider
import com.rodcollab.mymarvelcomics.core.database.dao.CharactersDao
import com.rodcollab.mymarvelcomics.core.database.dao.ComicsDao
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
    @Singleton
    @Provides
    fun providesComicsDao(database: AppDatabase): ComicsDao {
        return database.comicsDao()
    }
    @Singleton
    @Provides
    fun providesCharactersDao(database: AppDatabase): CharactersDao {
        return database.charactersDao()
    }
    @Singleton
    @Provides
    fun providesTransactionProvider(database: AppDatabase): TransactionProvider {
        return DefaultTransactionProvider(database)
    }
}
