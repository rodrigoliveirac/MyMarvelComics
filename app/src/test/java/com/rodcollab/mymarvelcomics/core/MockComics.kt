package com.rodcollab.mymarvelcomics.core

import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork
import com.rodcollab.mymarvelcomics.core.network.model.Thumbnail

fun comicsFromNetwork() = listOf(
    ComicNetwork(
        id = 82967,
        title = "Marvel Previews (2017)",
        description = "",
        pageCount = 112,
        resourceURI = "http://gateway.marvel.com/v1/public/comics/82967",
        thumbnail = Thumbnail(
            path = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available",
            extension = "jpg"
        ),
    ), ComicNetwork(
        id = 82965,
        title = "Marvel Previews (2017)",
        description = "",
        pageCount = 152,
        resourceURI = "http://gateway.marvel.com/v1/public/comics/82965",
        thumbnail = Thumbnail(
            path = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available",
            extension = "jpg"
        ),
    )
)
