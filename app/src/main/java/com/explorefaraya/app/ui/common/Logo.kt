package com.explorefaraya.app.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.explorefaraya.app.R

/** The real Faraya & Beyond logo (white mark + wordmark, transparent background). */
@Composable
fun FarayaLogoHeader(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.faraya_beyond_logo),
        contentDescription = "Faraya & Beyond",
        modifier = modifier.width(220.dp)
    )
}
