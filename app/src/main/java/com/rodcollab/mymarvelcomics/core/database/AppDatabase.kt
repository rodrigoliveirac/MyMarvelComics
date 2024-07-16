package com.rodcollab.mymarvelcomics.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rodcollab.mymarvelcomics.core.database.converters.CharacterListTypeConverter
import com.rodcollab.mymarvelcomics.core.database.converters.ContentSummaryTypeConverter
import com.rodcollab.mymarvelcomics.core.database.converters.ListContentSummaryTypeConverter
import com.rodcollab.mymarvelcomics.core.database.converters.ListImageTypeConverter
import com.rodcollab.mymarvelcomics.core.database.converters.ThumbnailTypeConverter
import com.rodcollab.mymarvelcomics.core.database.dao.FavoriteComicsDao
import com.rodcollab.mymarvelcomics.core.database.model.FavoriteComicEntity

@TypeConverters(
    CharacterListTypeConverter::class,
    ListContentSummaryTypeConverter::class,
    ListImageTypeConverter::class,
    ContentSummaryTypeConverter::class,
    ThumbnailTypeConverter::class
)
@Database(entities = [FavoriteComicEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteComicsDao(): FavoriteComicsDao

    companion object {

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return instance!!
        }

        private const val DATABASE_NAME = "app-database.db"
    }
}