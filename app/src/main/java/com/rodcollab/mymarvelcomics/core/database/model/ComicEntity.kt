package com.rodcollab.mymarvelcomics.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary
import com.rodcollab.mymarvelcomics.core.network.model.Image
import com.rodcollab.mymarvelcomics.core.network.model.ResourceList
import com.rodcollab.mymarvelcomics.core.network.model.Thumbnail

@Entity("comics")
data class ComicEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val remoteId: Int,
    val characters: List<Int>?,
    val collections: List<ContentSummary>?,
    val description: String?,
    val images: List<Image?>?,
    val pageCount: Int?,
    val resourceURI: String?,
    val thumbnail: Thumbnail?,
    val title: String?,
    val resourceList: ResourceList?
)