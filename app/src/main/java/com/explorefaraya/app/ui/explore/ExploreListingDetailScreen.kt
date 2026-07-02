package com.explorefaraya.app.ui.explore

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.explorefaraya.app.data.model.ExploreCatalog
import com.explorefaraya.app.ui.common.FarayaButton
import com.explorefaraya.app.ui.common.FarayaTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreListingDetailScreen(
    listingId: String,
    onBack: () -> Unit,
    onBookNow: (listingId: String, partySize: Int, scheduledFor: String) -> Unit
) {
    val listing = ExploreCatalog.findById(listingId) ?: return
    var partySize by remember { mutableIntStateOf(1) }
    var scheduledFor by remember { mutableStateOf("") }
    val accent = Color(android.graphics.Color.parseColor(listing.accentColorHex))
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(listing.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(accent)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(listing.category.label.uppercase(), style = MaterialTheme.typography.labelLarge, color = accent)
            Text(listing.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(listing.location, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(listing.description, style = MaterialTheme.typography.bodyLarge)
            if (listing.price > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "$${"%.2f".format(listing.price)} ${listing.priceUnit}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Book this", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            FarayaTextField(
                value = scheduledFor,
                onValueChange = { scheduledFor = it },
                label = "Preferred date & time (e.g. Sat, Aug 9 · 7:00 PM)"
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Party size / units", style = MaterialTheme.typography.bodyLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (partySize > 1) partySize-- }) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    Text("$partySize", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = { if (partySize < 20) partySize++ }) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            FarayaButton(
                text = "Book & Pay",
                onClick = { onBookNow(listing.id, partySize, scheduledFor) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(listing.link)))
            }) {
                Text("View on explorefaraya.com")
            }
        }
    }
}
