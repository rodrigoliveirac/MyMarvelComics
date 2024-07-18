package com.rodcollab.mymarvelcomics.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodcollab.mymarvelcomics.core.network.model.Thumbnail

@Entity("comics")
data class ComicEntity(
    val id: Long = 0L,
    @PrimaryKey val remoteId: Int,
    val characters: List<Int>?,
    val description: String?,
    val pageCount: Int?,
    val resourceURI: String?,
    val thumbnail: Thumbnail?,
    val title: String?,
)