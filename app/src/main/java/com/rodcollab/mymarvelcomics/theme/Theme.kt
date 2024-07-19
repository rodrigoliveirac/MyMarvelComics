package com.rodcollab.mymarvelcomics.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MyMarvelComicsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit,
) {

    MaterialTheme(
        colorScheme = getColorScheme(darkTheme),
        typography = Typography,
        content = content
    )
}
private fun getColorScheme(isDarkTheme: Boolean) =
    if (isDarkTheme) {
        ColorApp.DarkColors
    } else {
        ColorApp.LightColors
    }