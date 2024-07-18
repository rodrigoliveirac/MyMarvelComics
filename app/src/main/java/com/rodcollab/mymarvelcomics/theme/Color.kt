package com.rodcollab.mymarvelcomics.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object ColorApp {

    val Dark10 = Color(0x1A000000)
    val AlphaBlack = Color(0xEB000000)

    val Gray100 = Color(0xFF2C2C2C)

    val LightColors = lightColorScheme(
        primary = Color(0xFFE50914),
        secondary = Color(0xFFF5F5F1),
        background = Color(0xFFFFFFFF),
        onBackground = Color(0xFFFFFFFF),
        surface = Color(0xFFB80009),
        onSurface = Color(0xFFF5F5F1),
        onSurfaceVariant = Color(0xFFFFB3B7)
    )

    val DarkColors = darkColorScheme(
        primary = Color(0xFFE50914),
        secondary = Color(0xFFF5F5F1),
        background = Color(0xFF000000),
        onBackground = Color(0xFFFFFFFF),
        surface = Color(0xFF121212),
        onSurface = Color(0xFFF5F5F1),
        onSurfaceVariant = Color(0XFF7b7b7b)
    )

}
