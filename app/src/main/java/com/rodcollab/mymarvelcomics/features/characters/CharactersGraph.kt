package com.rodcollab.mymarvelcomics.features.characters

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.rodcollab.mymarvelcomics.R
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCDestinations
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCDestinationsArgs
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCScreens

sealed class NavItem(val route: String, val icon: Int, val label: String) {
    data object Characters : NavItem(MMCScreens.CHARACTERS_SCREEN, R.drawable.ic_hero, "Characters")
    data object Comics : NavItem(MMCScreens.COMICS_SCREEN, R.drawable.ic_comics, "Comics")
}

fun NavGraphBuilder.characters(navController: NavController) {

    composable(route = MMCDestinations.CHARACTERS_ROUTE) {
        val viewModel = hiltViewModel<CharactersViewModel>()
        val characters = viewModel.data.collectAsLazyPagingItems()
        CharactersScreen(
            toComics = { route ->
                       navController.navigate(route)
            },
            characters = characters,
            onRefresh = {
                viewModel.refresh()
            }) { characterId ->
            navController.navigate("${MMCScreens.CHARACTER_DETAILS_SCREEN}/$characterId")
        }
    }

    composable(
        route = MMCDestinations.CHARACTER_DETAILS_ROUTE,
        arguments = listOf(
            navArgument(MMCDestinationsArgs.CHARACTER_ID) { type = NavType.IntType },
        )
    ) {
        val viewModel = hiltViewModel<CharacterDetailsViewModel>()
        val comics = viewModel.comicsPaging.collectAsLazyPagingItems()
        val characterDetails by viewModel.uiState.collectAsState()
        CharacterDetailsScreen(characterDetails, comics, toComic = { comicId ->
            navController.navigate("${MMCScreens.COMIC_DETAILS_SCREEN}/$comicId")
        }) {
            navController.navigateUp()
        }
    }

}