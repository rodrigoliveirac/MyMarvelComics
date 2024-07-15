package com.rodcollab.mymarvelcomics.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodcollab.mymarvelcomics.core.network.model.CharacterList
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary

@Entity("favorite_comics")
data class FavoriteComicEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("favorite_comic_characters") val characters: CharacterList,
    @ColumnInfo("favorite_comic_collections") val collections: List<ContentSummary>,
    @ColumnInfo("favorite_comic_description") val description: String,
    @ColumnInfo("favorite_comic_images") val images: List<String>,
    @ColumnInfo("favorite_comic_pageCount") val pageCount: Int,
    @ColumnInfo("favorite_comic_resourceURI") val resourceURI: String,
    @ColumnInfo("favorite_comic_thumbnail") val thumbnail: String,
    @ColumnInfo("favorite_comic_title") val title: String,
)