package com.explorefaraya.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// The site (explorefaraya.com) is light-themed only, so the app always uses
// this scheme regardless of the device's system dark-mode setting, to stay
// visually consistent with the brand.
private val FarayaColors = lightColorScheme(
    primary = FarayaPrimary,
    onPrimary = FarayaWhite,
    secondary = FarayaHeading,
    onSecondary = FarayaWhite,
    tertiary = FarayaYellow,
    onTertiary = FarayaBlack,
    background = FarayaWhite,
    onBackground = FarayaBody,
    surface = FarayaWhite,
    onSurface = FarayaBody,
    surfaceVariant = FarayaSmoke2,
    onSurfaceVariant = FarayaBody,
    outline = FarayaBorder,
    error = FarayaError,
)

@Composable
fun ExploreFarayaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FarayaColors,
        typography = ExploreFarayaTypography,
        content = content
    )
}
