package com.rodcollab.mymarvelcomics.features.characters

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

fun NavGraphBuilder.characters(navController:NavController) {

    composable(route = MMCDestinations.CHARACTERS_ROUTE) {
        val viewModel = hiltViewModel<CharactersViewModel>()
        val characters = viewModel.data.collectAsLazyPagingItems()
        CharactersScreen(characters = characters) { characterId ->
            navController.navigate("${MMCScreens.CHARACTER_DETAILS_SCREEN}/$characterId")
        }
    }

    composable(
        route = MMCDestinations.CHARACTER_DETAILS_ROUTE,
        arguments = listOf(
            navArgument(MMCDestinationsArgs.CHARACTER_ID) { type = NavType.IntType; nullable = false },
        )
    ) {
//        val viewModel = hiltViewModel<CharacterDetailsViewModel>()
//
//        CharacterDetailsScreen()
    }

}