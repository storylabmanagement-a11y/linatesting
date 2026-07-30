package com.explorefaraya.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.explorefaraya.app.R

// Editorial serif for headers, clean sans for body — "boutique concierge", not
// generic tourism-board styling.
val FBSerif = FontFamily(
    Font(R.font.playfair_regular, FontWeight.Normal),
    Font(R.font.playfair_bold, FontWeight.Bold),
    Font(R.font.playfair_black, FontWeight.Black)
)

val FBSans = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val ExploreFarayaTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FBSerif,
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        color = FBOffWhite
    ),
    headlineMedium = TextStyle(
        fontFamily = FBSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        color = FBOffWhite
    ),
    titleLarge = TextStyle(
        fontFamily = FBSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        color = FBOffWhite
    ),
    titleMedium = TextStyle(
        fontFamily = FBSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        color = FBOffWhite
    ),
    bodyLarge = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = FBMutedGray
    ),
    bodyMedium = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = FBMutedGray
    ),
    labelLarge = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 1.2.sp,
        color = FBGold
    ),
)
