package com.rodcollab.mymarvelcomics.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("characters")
data class CharacterEntity(
    val id: Long = 0L,
    @PrimaryKey val remoteId: Int,
    @ColumnInfo("character_name") val name: String,
    @ColumnInfo("character_description")val description: String?,
    @ColumnInfo("character_thumbnail") val thumbnail: String?,
    @ColumnInfo("character_comic") val comics: List<Int>?,
    @ColumnInfo("character_resourceURI") val resourceURI: String
)