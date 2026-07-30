package com.explorefaraya.app.ui.explore

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.explorefaraya.app.data.model.ExploreCatalog
import com.explorefaraya.app.ui.common.FarayaButton
import com.explorefaraya.app.ui.common.FarayaTextField
import com.explorefaraya.app.ui.saved.BookmarksViewModel
import com.explorefaraya.app.ui.theme.FBGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteDetailScreen(
    siteId: String,
    onBack: () -> Unit,
    onBookNow: (siteId: String, partySize: Int, scheduledFor: String) -> Unit,
    bookmarksViewModel: BookmarksViewModel = viewModel()
) {
    val site = ExploreCatalog.findById(siteId) ?: return
    val context = LocalContext.current
    val bookmarks by bookmarksViewModel.bookmarks.collectAsState()
    val isBookmarked = siteId in bookmarks.siteIds

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(site.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { bookmarksViewModel.toggleSite(siteId) }) {
                        Icon(
                            if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = FBGold
                        )
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
        ) {
            if (site.photos.isNotEmpty()) {
                LazyRow {
                    items(site.photos) { photo ->
                        AsyncImage(
                            model = photo,
                            contentDescription = site.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(220.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(site.category.label.uppercase(), style = MaterialTheme.typography.labelLarge)
                Text(site.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(site.longDescription, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(20.dp))
                InfoRow("Hours", site.hours)
                if (site.entryFee.isNotBlank()) InfoRow("Entry", site.entryFee)
                InfoRow("From Beirut", site.driveTimeBeirut)
                InfoRow("From Jounieh", site.driveTimeJounieh)

                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ListingActions.mapsSearchUri(site.name))))
                    }) {
                        Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                        Text("Get Directions")
                    }
                    val phone = ListingActions.primaryPhone(site.phone)
                    if (phone != null) {
                        OutlinedButton(onClick = {
                            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(ListingActions.telUri(phone))))
                        }) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                            Text("Call")
                        }
                    }
                }

                if (site.bookable) {
                    Spacer(modifier = Modifier.height(28.dp))
                    BookingSection(siteId = site.id, onBookNow = onBookNow)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun BookingSection(
    siteId: String,
    onBookNow: (siteId: String, partySize: Int, scheduledFor: String) -> Unit
) {
    var partySize by remember { mutableIntStateOf(1) }
    var scheduledFor by remember { mutableStateOf("") }

    Text("Reserve", style = MaterialTheme.typography.titleMedium)
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
        Text("Party size", style = MaterialTheme.typography.bodyLarge)
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
    FarayaButton(text = "Reserve", onClick = { onBookNow(siteId, partySize, scheduledFor) })
}
