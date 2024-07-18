package com.rodcollab.mymarvelcomics.features.characters

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.rodcollab.mymarvelcomics.core.ui.navigation.MMCDestinations

fun NavGraphBuilder.characters() {
    composable(route = MMCDestinations.CHARACTERS) {
        val viewModel = hiltViewModel<CharactersViewModel>()
        val characters = viewModel.data.collectAsLazyPagingItems()
        CharactersScreen(characters = characters)
    }
}