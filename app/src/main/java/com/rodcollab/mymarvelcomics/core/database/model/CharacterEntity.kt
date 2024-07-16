package com.rodcollab.mymarvelcomics.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodcollab.mymarvelcomics.core.network.model.Image

@Entity("characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo("character_name") val name: String,
    @ColumnInfo("character_description")val description: String,
    @ColumnInfo("character_thumbnail") val thumbnail: Image,
    @ColumnInfo("character_comic") val comics: List<ComicEntity?>
)