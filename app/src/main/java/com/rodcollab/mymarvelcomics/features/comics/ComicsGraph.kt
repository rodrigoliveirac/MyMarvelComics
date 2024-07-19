package com.rodcollab.mymarvelcomics.features.comics

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCDestinations
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCDestinationsArgs
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCScreens

fun NavGraphBuilder.comics(navController: NavController) {

    composable(route = MMCDestinations.COMICS_ROUTE) {
        val viewModel = hiltViewModel<ComicsViewModel>()
        val comics = viewModel.allComics.collectAsLazyPagingItems()
        val dropDownMenu by viewModel.dropDownMenu.collectAsState()
        val favorites by viewModel.favorites.collectAsState()
        ComicsScreen(
            dropDownExpanded = dropDownMenu.expanded,
            expandedDropdownMenu = {
             viewModel.expandedDropdownMenu()
            },
            uiOptions = dropDownMenu,
            toFilter = {
                viewModel.switchSession(it)
            },
            toCharacters = { route ->
                navController.navigate(route)
            },
            favorites = favorites.model ?: emptyList(),
            comics = comics,
            onRefresh = {
                viewModel.refresh()
            }) { comicId ->
            navController.navigate("${MMCScreens.COMIC_DETAILS_SCREEN}/$comicId")
        }
    }

    composable(
        route = MMCDestinations.COMIC_DETAILS_ROUTE,
        arguments = listOf(
            navArgument(MMCDestinationsArgs.COMIC_ID) { type = NavType.IntType },
        )
    ) {
        val viewModel = hiltViewModel<ComicDetailsViewModel>()
        val characters = viewModel.characters.collectAsLazyPagingItems()
        val comicDetails by viewModel.uiState.collectAsState()
        ComicDetailsScreen(onFavorite = {
            viewModel.checkIfIsFavorite()
        }, onCancel = {
            viewModel.cancel()
        },
            onConfirm = {
                viewModel.removeFromFavorites()
            },
            comicDetails, characters
        ) {
            navController.navigateUp()
        }
    }

}