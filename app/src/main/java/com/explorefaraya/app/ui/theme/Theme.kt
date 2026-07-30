package com.explorefaraya.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// "Faraya & Beyond" is a black/gold editorial identity — always dark, regardless
// of the device's system theme, to stay on-brand.
private val FBColors = darkColorScheme(
    primary = FBGold,
    onPrimary = FBBlack,
    secondary = FBGoldSoft,
    onSecondary = FBBlack,
    tertiary = FBGold,
    onTertiary = FBBlack,
    background = FBBlack,
    onBackground = FBOffWhite,
    surface = FBSurface,
    onSurface = FBOffWhite,
    surfaceVariant = FBSurface,
    onSurfaceVariant = FBMutedGray,
    outline = FBBorder,
    error = FBError,
)

@Composable
fun ExploreFarayaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FBColors,
        typography = ExploreFarayaTypography,
        content = content
    )
}
