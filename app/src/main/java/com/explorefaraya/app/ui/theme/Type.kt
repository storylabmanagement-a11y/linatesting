package com.explorefaraya.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.explorefaraya.app.R

// Matches explorefaraya.com's Themeholy "Tourm" theme fonts: Manrope for
// headings, Inter for body text, Montez (cursive) for decorative taglines.
val FarayaManrope = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold)
)

val FarayaInter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val FarayaMontez = FontFamily(
    Font(R.font.montez_regular, FontWeight.Normal)
)

val ExploreFarayaTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FarayaManrope,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        color = FarayaHeading
    ),
    headlineMedium = TextStyle(
        fontFamily = FarayaManrope,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        color = FarayaHeading
    ),
    titleLarge = TextStyle(
        fontFamily = FarayaManrope,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        color = FarayaHeading
    ),
    titleMedium = TextStyle(
        fontFamily = FarayaManrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        color = FarayaHeading
    ),
    bodyLarge = TextStyle(
        fontFamily = FarayaInter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = FarayaBody
    ),
    bodyMedium = TextStyle(
        fontFamily = FarayaInter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = FarayaBody
    ),
    labelLarge = TextStyle(
        fontFamily = FarayaInter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = FarayaPrimary
    ),
)
