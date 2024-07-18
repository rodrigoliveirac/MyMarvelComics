package com.rodcollab.mymarvelcomics.core.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rodcollab.mymarvelcomics.features.characters.characters
import com.rodcollab.mymarvelcomics.features.comics.comics

@Composable
fun MMCNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MMCDestinations.CHARACTERS_ROUTE,
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = modifier
            ) {
                characters(navController)
                comics(navController)
            }
        }

    }

}