package com.rodcollab.mymarvelcomics.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity

@Dao
interface ComicsDao {

    @Insert
    suspend fun upsertAll(comics: List<ComicEntity?>)

    @Query("SELECT * FROM comics ORDER BY id ASC")
    fun comicsPagingSource(): PagingSource<Int, ComicEntity>

    @Query("DELETE FROM comics")
    suspend fun clearAll()
}