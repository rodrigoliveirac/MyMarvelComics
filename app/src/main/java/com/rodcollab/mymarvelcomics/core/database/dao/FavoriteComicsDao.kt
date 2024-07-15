package com.rodcollab.mymarvelcomics.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rodcollab.mymarvelcomics.core.database.model.FavoriteComicEntity

@Dao
interface FavoriteComicsDao {

    @Query("SELECT * FROM favorite_comics")
    suspend fun fetchFavoriteComics() : List<FavoriteComicEntity>

    @Query("SELECT * FROM favorite_comics WHERE id = :id")
    suspend fun readFavoriteComic(id: Int) : FavoriteComicEntity

    @Query("DELETE FROM favorite_comics WHERE id = :id")
    suspend fun deleteFavoriteComic(id: Int)

    @Insert
    suspend fun addFavoriteComic(comic: FavoriteComicEntity)
}