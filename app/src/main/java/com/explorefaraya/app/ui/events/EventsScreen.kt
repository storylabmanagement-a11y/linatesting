package com.explorefaraya.app.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.explorefaraya.app.data.model.EventCatalog
import com.explorefaraya.app.data.model.EventItem
import com.explorefaraya.app.ui.theme.FBGold
import com.explorefaraya.app.ui.theme.FBCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(onEventClick: (String) -> Unit) {
    val featured = EventCatalog.featured()
    val all = EventCatalog.events

    Scaffold(
        topBar = { TopAppBar(title = { Text("Events") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (featured.isNotEmpty()) {
                item {
                    Text("Featured Events", style = MaterialTheme.typography.titleLarge)
                }
            }
            item {
                Text("All Events", style = MaterialTheme.typography.titleLarge)
            }
            items(all, key = { it.id }) { event ->
                EventCard(event, onClick = { onEventClick(event.id) })
            }
        }
    }
}

@Composable
fun EventCard(event: EventItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = FBCard),
        border = if (event.featured) androidx.compose.foundation.BorderStroke(1.dp, FBGold) else null
    ) {
        Column {
            if (event.featured) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.padding(12.dp)
                ) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .background(FBGold, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("FEATURED", style = MaterialTheme.typography.labelLarge.copy(color = androidx.compose.ui.graphics.Color.Black))
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp).padding(top = if (event.featured) 0.dp else 4.dp)) {
                Text(event.category.uppercase(), style = MaterialTheme.typography.labelLarge)
                Text(event.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(event.date, style = MaterialTheme.typography.bodyMedium)
                Text(event.venue, style = MaterialTheme.typography.bodyMedium)
                event.tiers.minByOrNull { it.price }?.let {
                    Text(
                        "From $${"%.0f".format(it.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = FBGold
                    )
                }
            }
        }
    }
}
