package com.rodcollab.mymarvelcomics.core

import com.rodcollab.mymarvelcomics.core.network.model.CharacterNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary
import com.rodcollab.mymarvelcomics.core.network.model.Image
import com.rodcollab.mymarvelcomics.core.network.model.ResourceList

val characters = listOf(
    CharacterNetwork(
        id = 1011334,
        name = "3-D Man",
        description = "",
        thumbnail = Image(
            path = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784",
            extension = "jpg"
        ),
        comics = ResourceList(
            available = 12,
            collectionURI = "http://gateway.marvel.com/v1/public/characters/1011334/comics",
            items = listOf(
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/21366",
                    name = "Avengers: The Initiative (2007) #14"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/24571",
                    name = "Avengers: The Initiative (2007) #14 (SPOTLIGHT VARIANT)"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/21546",
                    name = "Avengers: The Initiative (2007) #15"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/21741",
                    name = "Avengers: The Initiative (2007) #16"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/21975",
                    name = "Avengers: The Initiative (2007) #17"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/22299",
                    name = "Avengers: The Initiative (2007) #18"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/22300",
                    name = "Avengers: The Initiative (2007) #18 (ZOMBIE VARIANT)"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/22506",
                    name = "Avengers: The Initiative (2007) #19"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/8500",
                    name = "Deadpool (1997) #44"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/10223",
                    name = "Marvel Premiere (1972) #35"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/10224",
                    name = "Marvel Premiere (1972) #36"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/10225",
                    name = "Marvel Premiere (1972) #37"
                )
            ),
        )
    ),
    CharacterNetwork(
        id = 1017100,
        name = "A-Bomb (HAS)",
        description = "Rick Jones has been Hulk's best bud since day one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of destruction!",
        thumbnail = Image(
            path = "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16",
            extension = "jpg"
        ),
        comics = ResourceList(
            available = 4,
            collectionURI = "http://gateway.marvel.com/v1/public/characters/1017100/comics",
            items = listOf(
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/47176",
                    name = "FREE COMIC BOOK DAY 2013 1 (2013) #1"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/40632",
                    name = "Hulk (2008) #53"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/40630",
                    name = "Hulk (2008) #54"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/40628",
                    name = "Hulk (2008) #55"
                )
            ),
        )
    ),
    CharacterNetwork(
        id = 1009144,
        name = "A.I.M.",
        description = "AIM is a terrorist organization bent on destroying the world.",
        thumbnail = Image(
            path = "http://i.annihil.us/u/prod/marvel/i/mg/6/20/52602f21f29ec",
            extension = "jpg"
        ),
        comics = ResourceList(
            available = 7,
            collectionURI = "http://gateway.marvel.com/v1/public/characters/1009144/comics",
            items = listOf(

                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/7513",
                    name = "Captain America (1968) #132"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/7514",
                    name = "Captain America (1968) #133"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/65466",
                    name = "Captain America by Mark Waid, Ron Garney & Andy Kubert (Hardcover)"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/20367",
                    name = "Defenders (1972) #57"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/31068",
                    name = "Incredible Hulks (2010) #606 (HA 606-B)"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/30090",
                    name = "Incredible Hulks (2010) #606 (G 606-I)"
                ),
                ContentSummary(
                    resourceURI = "http://gateway.marvel.com/v1/public/comics/49629",
                    name = "Indestructible Hulk (2012) #14"
                )
            ),
        )
    )
)
