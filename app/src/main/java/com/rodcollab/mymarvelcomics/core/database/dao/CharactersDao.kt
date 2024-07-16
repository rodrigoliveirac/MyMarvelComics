package com.rodcollab.mymarvelcomics.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity

@Dao
interface CharactersDao {

    @Insert
    suspend fun upsertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun charactersPagingSource(): PagingSource<Int, CharacterEntity>

    @Query("DELETE FROM characters")
    suspend fun clearAll()
}