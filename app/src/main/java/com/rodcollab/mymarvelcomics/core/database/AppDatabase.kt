package com.rodcollab.mymarvelcomics.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rodcollab.mymarvelcomics.core.database.converters.ResourceListTypeConverter
import com.rodcollab.mymarvelcomics.core.database.converters.ContentSummaryTypeConverter
import com.rodcollab.mymarvelcomics.core.database.converters.ListContentSummaryTypeConverter
import com.rodcollab.mymarvelcomics.core.database.converters.ListImageTypeConverter
import com.rodcollab.mymarvelcomics.core.database.converters.ListIntTypeConverter
import com.rodcollab.mymarvelcomics.core.database.converters.ThumbnailTypeConverter
import com.rodcollab.mymarvelcomics.core.database.dao.CharactersDao
import com.rodcollab.mymarvelcomics.core.database.dao.ComicsDao
import com.rodcollab.mymarvelcomics.core.database.dao.FavoriteComicsDao
import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity
import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity
import com.rodcollab.mymarvelcomics.core.database.model.FavoriteComicEntity

@TypeConverters(
    ResourceListTypeConverter::class,
    ListContentSummaryTypeConverter::class,
    ListImageTypeConverter::class,
    ContentSummaryTypeConverter::class,
    ThumbnailTypeConverter::class,
    ListIntTypeConverter::class
)
@Database(entities = [FavoriteComicEntity::class,ComicEntity::class, CharacterEntity::class], version = 2, exportSchema = false)
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