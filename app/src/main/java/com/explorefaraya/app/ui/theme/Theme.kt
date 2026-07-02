package com.explorefaraya.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = FarayaBlue,
    secondary = FarayaTeal,
    tertiary = FarayaOrange,
    background = FarayaSnow,
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onBackground = FarayaInk,
    onSurface = FarayaInk,
    error = FarayaError,
)

private val DarkColors = darkColorScheme(
    primary = FarayaTeal,
    secondary = FarayaBlue,
    tertiary = FarayaOrange,
    background = FarayaInk,
    surface = Color(0xFF1A2C38),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onBackground = FarayaSnow,
    onSurface = FarayaSnow,
    error = FarayaError,
)

@Composable
fun ExploreFarayaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = ExploreFarayaTypography,
        content = content
    )
}
