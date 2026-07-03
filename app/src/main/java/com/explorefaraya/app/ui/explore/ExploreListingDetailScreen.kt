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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Language
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
    val isBookable = ExploreCatalog.isBookable(listing.category)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(listing.name) },
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
        ) {
            if (listing.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = listing.imageUrl,
                    contentDescription = listing.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(listing.category.uppercase(), style = MaterialTheme.typography.labelLarge)
                Text(listing.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                if (listing.phone.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(listing.phone, style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(20.dp))
                ContactActionsRow(listing.name, listing.phone, listing.linkType)

                if (isBookable) {
                    Spacer(modifier = Modifier.height(28.dp))
                    BookingSection(
                        listingId = listing.id,
                        onBookNow = onBookNow
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactActionsRow(name: String, phone: String, linkType: String) {
    val context = LocalContext.current
    val primaryPhone = ListingActions.primaryPhone(phone)

    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (primaryPhone != null) {
                OutlinedButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(ListingActions.telUri(primaryPhone))))
                }) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                    Text("Call")
                }
            }
            if (ListingActions.hasDirections(linkType)) {
                OutlinedButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ListingActions.mapsSearchUri(name))))
                }) {
                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                    Text("Directions")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (primaryPhone != null && ListingActions.hasWhatsApp(linkType)) {
                OutlinedButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ListingActions.whatsAppUri(primaryPhone))))
                }) {
                    Text("WhatsApp")
                }
            }
            ListingActions.websiteUrl(linkType)?.let { url ->
                OutlinedButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }) {
                    Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                    Text("Website")
                }
            }
            ListingActions.instagramUrl(linkType)?.let { url ->
                OutlinedButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }) {
                    Text("Instagram")
                }
            }
        }
    }
}

@Composable
private fun BookingSection(
    listingId: String,
    onBookNow: (listingId: String, partySize: Int, scheduledFor: String) -> Unit
) {
    var partySize by remember { mutableIntStateOf(1) }
    var scheduledFor by remember { mutableStateOf("") }

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
        onClick = { onBookNow(listingId, partySize, scheduledFor) }
    )
}
