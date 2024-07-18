package com.rodcollab.mymarvelcomics.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodcollab.mymarvelcomics.core.network.model.Thumbnail

@Entity("comics")
data class ComicEntity(
    @PrimaryKey val id: Int,
    val description: String?,
    val pageCount: Int?,
    val resourceURI: String?,
    val thumbnail: Thumbnail?,
    val title: String?,
)