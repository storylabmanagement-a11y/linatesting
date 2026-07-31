package com.explorefaraya.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.explorefaraya.app.R

// Single clean sans-serif throughout, matching the approved mockups —
// no serif display font anywhere in the design.
val FBSans = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val ExploreFarayaTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        color = FBOffWhite
    ),
    headlineMedium = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = FBOffWhite
    ),
    titleLarge = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 23.sp,
        color = FBOffWhite
    ),
    titleMedium = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = FBOffWhite
    ),
    bodyLarge = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        color = FBMutedGray
    ),
    bodyMedium = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.5.sp,
        lineHeight = 18.sp,
        color = FBMutedGold
    ),
    labelLarge = TextStyle(
        fontFamily = FBSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.3.sp,
        color = FBGold
    ),
)
