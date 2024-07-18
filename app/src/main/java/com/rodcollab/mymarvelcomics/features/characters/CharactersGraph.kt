package com.rodcollab.mymarvelcomics.features.characters

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

fun NavGraphBuilder.characters(navController: NavController) {

    composable(route = MMCDestinations.CHARACTERS_ROUTE) {
        val viewModel = hiltViewModel<CharactersViewModel>()
        val characters = viewModel.data.collectAsLazyPagingItems()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet { /* Drawer content */ }
            },
        ) {
            CharactersScreen(
                onModalNav = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                },
                characters = characters,
                onRefresh = {
                viewModel.refresh()
            }) { characterId ->
                navController.navigate("${MMCScreens.CHARACTER_DETAILS_SCREEN}/$characterId")
            }
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

//private fun navigateFromMenu(
//    it: MenuActions,
//    navController: NavController,
//) {
//    when (it) {
//        MenuActions.ToCharacters -> {
//            navController.navigate(MMCDestinations.CHARACTERS_ROUTE)
//        }
//
//        else -> {
//            navController.navigate(MMCDestinations.COMICS_ROUTE)
//        }
//    }
//}