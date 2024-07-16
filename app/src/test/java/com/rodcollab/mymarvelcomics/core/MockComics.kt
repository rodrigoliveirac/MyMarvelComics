package com.rodcollab.mymarvelcomics.core

import com.rodcollab.mymarvelcomics.core.network.model.CharacterList
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork
import com.rodcollab.mymarvelcomics.core.network.model.Thumbnail

fun comicsFromNetwork() = listOf(
    ComicNetwork(
        id = 82967,
        title = "Marvel Previews (2017)",
        description = "",
        pageCount = 112,
        resourceURI = "http://gateway.marvel.com/v1/public/comics/82967",
        collections = emptyList(),
        thumbnail = Thumbnail(
            path = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available",
            extension = "jpg"
        ),
        images = emptyList(),
        characters = CharacterList(
            available = 0,
            collectionURI = "http://gateway.marvel.com/v1/public/comics/82967/characters",
            items = emptyList()
        )
    ), ComicNetwork(
        id = 82965,
        title = "Marvel Previews (2017)",
        description = "",
        pageCount = 152,
        resourceURI = "http://gateway.marvel.com/v1/public/comics/82965",
        collections = emptyList(),
        thumbnail = Thumbnail(
            path = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available",
            extension = "jpg"
        ),
        images = emptyList(),
        characters = CharacterList(
            available = 0,
            collectionURI = "http://gateway.marvel.com/v1/public/comics/82965/characters",
            items = emptyList()
        )
    )
)