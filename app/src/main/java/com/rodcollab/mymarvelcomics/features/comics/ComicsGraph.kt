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

fun NavGraphBuilder.comics(navController: NavController) {

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