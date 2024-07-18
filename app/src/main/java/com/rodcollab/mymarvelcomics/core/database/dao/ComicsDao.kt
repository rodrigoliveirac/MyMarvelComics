package com.rodcollab.mymarvelcomics.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity

@Dao
interface ComicsDao {

    @Upsert
    suspend fun upsertAll(comics: List<ComicEntity>)

    @Query("SELECT * FROM comics ORDER BY remoteId ASC")
    fun comicsPagingSource(): PagingSource<Int, ComicEntity>

    @Query("SELECT * FROM comics WHERE remoteId = :id")
    suspend fun comicById(id: Int): ComicEntity

    @Query("DELETE FROM comics")
    suspend fun clearAll()
}