package com.explorefaraya.app.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.explorefaraya.app.data.model.EventCatalog
import com.explorefaraya.app.data.model.EventItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(onEventClick: (String) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { androidx.compose.material3.Text("Events & Activities") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(EventCatalog.events, key = { it.id }) { event ->
                EventCard(event = event, onClick = { onEventClick(event.id) })
            }
        }
    }
}

@Composable
fun EventCard(event: EventItem, onClick: () -> Unit) {
    val accent = Color(android.graphics.Color.parseColor(event.accentColorHex))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(accent)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = event.category.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = accent
                )
                Text(event.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(event.date, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(event.location, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
