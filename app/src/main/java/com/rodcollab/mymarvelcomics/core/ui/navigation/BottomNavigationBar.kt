package com.rodcollab.mymarvelcomics.core.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rodcollab.mymarvelcomics.R

sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    data object Characters : BottomNavItem(MMCScreens.CHARACTERS_SCREEN, R.drawable.ic_hero, "heros")
    data object Comics : BottomNavItem(MMCScreens.COMICS_SCREEN, R.drawable.ic_comics, "comics")
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val routes = listOf(BottomNavItem.Characters, BottomNavItem.Comics)
    BottomAppBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Spacer(modifier = Modifier.weight(1f))
        routes.forEach { item ->
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(modifier = Modifier.size(24.dp),onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null
                    )
                }
                Text(text = item.label, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

