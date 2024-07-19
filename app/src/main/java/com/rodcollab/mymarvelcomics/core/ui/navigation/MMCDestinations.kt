package com.rodcollab.mymarvelcomics.core.ui.navigation

import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCDestinationsArgs.CHARACTER_ID
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCDestinationsArgs.COMIC_ID
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCScreens.CHARACTERS_SCREEN
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCScreens.CHARACTER_DETAILS_SCREEN
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCScreens.COMICS_SCREEN
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCScreens.COMIC_DETAILS_SCREEN

object MMCScreens {
    const val CHARACTERS_SCREEN = "characters"
    const val CHARACTER_DETAILS_SCREEN = "character"
    const val COMICS_SCREEN = "comics"
    const val COMIC_DETAILS_SCREEN = "comic"
}

object MMCDestinationsArgs {
    const val CHARACTER_ID = "characterId"
    const val COMIC_ID = "comicId"
}

object MMCDestinations {
    const val CHARACTERS_ROUTE = CHARACTERS_SCREEN
    const val CHARACTER_DETAILS_ROUTE =  "$CHARACTER_DETAILS_SCREEN/{$CHARACTER_ID}"
    const val COMICS_ROUTE = COMICS_SCREEN
    const val COMIC_DETAILS_ROUTE = "$COMIC_DETAILS_SCREEN/{$COMIC_ID}"
}