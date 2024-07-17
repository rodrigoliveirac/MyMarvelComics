package com.rodcollab.mymarvelcomics.ui.theme

import android.app.Activity
import android.os.Build
import android.provider.CalendarContract
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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