package com.explorefaraya.app.ui.events

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import com.explorefaraya.app.data.model.EventCatalog
import com.explorefaraya.app.data.model.TicketTier
import com.explorefaraya.app.ui.common.FarayaButton
import com.explorefaraya.app.ui.saved.BookmarksViewModel
import com.explorefaraya.app.ui.theme.FBGold
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    onBack: () -> Unit,
    onBuyTickets: (eventId: String, tierName: String, quantity: Int) -> Unit,
    viewModel: BookmarksViewModel = viewModel()
) {
    val event = EventCatalog.findById(eventId) ?: return
    val context = LocalContext.current
    val bookmarks by viewModel.bookmarks.collectAsState()
    val isBookmarked = eventId in bookmarks.eventIds

    var selectedTier by remember { mutableStateOf(event.tiers.firstOrNull()) }
    var quantity by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(event.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleEvent(eventId) }) {
                        Icon(
                            if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = FBGold
                        )
                    }
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "${event.title} — ${event.date} at ${event.venue}")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share event"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
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
            if (event.photos.isNotEmpty()) {
                LazyRow {
                    items(event.photos) { photo ->
                        AsyncImage(
                            model = photo,
                            contentDescription = event.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(200.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(event.category.uppercase(), style = MaterialTheme.typography.labelLarge)
                Text(event.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(event.date, style = MaterialTheme.typography.bodyLarge)
                Text(event.venue, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))
                CountdownTimer(event.dateTimeMillis)

                Spacer(modifier = Modifier.height(16.dp))
                Text(event.description, style = MaterialTheme.typography.bodyLarge)

                if (event.lineup.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Lineup", style = MaterialTheme.typography.titleMedium)
                    Text(event.lineup.joinToString(" · "), style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = {
                    val intent = Intent(Intent.ACTION_INSERT).apply {
                        data = CalendarContract.Events.CONTENT_URI
                        putExtra(CalendarContract.Events.TITLE, event.title)
                        putExtra(CalendarContract.Events.EVENT_LOCATION, event.venue)
                        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.dateTimeMillis)
                        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.dateTimeMillis + 3 * 60 * 60 * 1000)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Add to Calendar")
                }

                if (event.tiers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Tickets", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    event.tiers.forEach { tier ->
                        TierRow(
                            tier = tier,
                            selected = tier == selectedTier,
                            onSelect = { selectedTier = tier }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Quantity", style = MaterialTheme.typography.bodyLarge)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text("$quantity", style = MaterialTheme.typography.titleLarge)
                            IconButton(onClick = { if (quantity < 10) quantity++ }) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    val tier = selectedTier
                    FarayaButton(
                        text = if (tier != null) "Buy Tickets — $${"%.0f".format(tier.price * quantity)}" else "Select a tier",
                        enabled = tier != null,
                        onClick = { tier?.let { onBuyTickets(event.id, it.name, quantity) } }
                    )
                }
            }
        }
    }
}

@Composable
private fun TierRow(tier: TicketTier, selected: Boolean, onSelect: () -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onSelect),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = com.explorefaraya.app.ui.theme.FBCard
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) FBGold else com.explorefaraya.app.ui.theme.FBBorder
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(tier.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (tier.description.isNotBlank()) {
                    Text(tier.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Text(
                "$${"%.0f".format(tier.price)}",
                style = MaterialTheme.typography.titleMedium,
                color = if (selected) FBGold else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CountdownTimer(targetMillis: Long) {
    var remaining by remember { mutableLongStateOf(targetMillis - System.currentTimeMillis()) }

    LaunchedEffect(targetMillis) {
        while (true) {
            remaining = targetMillis - System.currentTimeMillis()
            delay(1000)
        }
    }

    if (remaining > 0) {
        val days = remaining / (1000 * 60 * 60 * 24)
        val hours = (remaining / (1000 * 60 * 60)) % 24
        val minutes = (remaining / (1000 * 60)) % 60
        Text(
            "Starts in ${days}d ${hours}h ${minutes}m",
            style = MaterialTheme.typography.titleMedium,
            color = FBGold
        )
    } else {
        Text("Event has started", style = MaterialTheme.typography.titleMedium, color = FBGold)
    }
}
