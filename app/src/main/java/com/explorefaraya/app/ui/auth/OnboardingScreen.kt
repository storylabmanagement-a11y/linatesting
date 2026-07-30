package com.explorefaraya.app.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.explorefaraya.app.ui.common.FarayaButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

private val INTERESTS = listOf("Nature", "Nightlife", "Wellness", "Adventure", "Dining", "Ski & Snow", "Cultural", "Family")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    var selected by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Personalize Your Feed") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            Text(
                "Pick a few interests so we can tailor your Home feed and event picks.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f)
            ) {
                items(INTERESTS) { interest ->
                    FilterChip(
                        selected = interest in selected,
                        onClick = {
                            selected = if (interest in selected) selected - interest else selected + interest
                        },
                        label = { Text(interest) },
                        modifier = Modifier.padding(4.dp).fillMaxWidth()
                    )
                }
            }
            FarayaButton(
                text = "Continue",
                onClick = {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                            .set(mapOf("interests" to selected.toList()), SetOptions.merge())
                    }
                    onDone()
                }
            )
        }
    }
}
