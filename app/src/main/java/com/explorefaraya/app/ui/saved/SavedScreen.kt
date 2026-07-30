package com.explorefaraya.app.ui.saved

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.explorefaraya.app.data.model.EventCatalog
import com.explorefaraya.app.ui.theme.FBCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    onSiteClick: (String) -> Unit,
    onEventClick: (String) -> Unit,
    viewModel: BookmarksViewModel = viewModel()
) {
    val bookmarks by viewModel.bookmarks.collectAsState()
    val savedEvents = EventCatalog.events.filter { it.id in bookmarks.eventIds }
    val savedSites = com.explorefaraya.app.data.model.ExploreCatalog.sites.filter { it.id in bookmarks.siteIds }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Saved") }) }
    ) { padding ->
        if (savedEvents.isEmpty() && savedSites.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
                Text(
                    "Nothing saved yet. Tap the bookmark icon on any place or event to save it here.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (savedEvents.isNotEmpty()) {
                    item { Text("Events", style = MaterialTheme.typography.titleMedium) }
                    items(savedEvents, key = { "e-" + it.id }) { event ->
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable { onEventClick(event.id) },
                            colors = CardDefaults.cardColors(containerColor = FBCard)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(event.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(event.date, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                if (savedSites.isNotEmpty()) {
                    item { Text("Places", style = MaterialTheme.typography.titleMedium) }
                    items(savedSites, key = { "s-" + it.id }) { site ->
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable { onSiteClick(site.id) },
                            colors = CardDefaults.cardColors(containerColor = FBCard)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(site.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(site.category.label, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
