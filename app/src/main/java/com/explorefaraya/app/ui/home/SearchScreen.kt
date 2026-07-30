package com.explorefaraya.app.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.explorefaraya.app.data.model.EventCatalog
import com.explorefaraya.app.data.model.ExploreCatalog
import com.explorefaraya.app.ui.common.FarayaTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onBack: () -> Unit, onSiteClick: (String) -> Unit, onEventClick: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val matchedSites = ExploreCatalog.sites.filter { it.name.contains(query, ignoreCase = true) && query.isNotBlank() }
    val matchedEvents = EventCatalog.events.filter { it.title.contains(query, ignoreCase = true) && query.isNotBlank() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            FarayaTextField(value = query, onValueChange = { query = it }, label = "Places & events")
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(6.dp))
            LazyColumn(
                contentPadding = PaddingValues(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(matchedEvents) { event ->
                    Card(modifier = Modifier.fillMaxWidth().clickable { onEventClick(event.id) }, colors = CardDefaults.cardColors()) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("EVENT", style = MaterialTheme.typography.labelLarge)
                            Text(event.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                items(matchedSites) { site ->
                    Card(modifier = Modifier.fillMaxWidth().clickable { onSiteClick(site.id) }, colors = CardDefaults.cardColors()) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(site.category.label.uppercase(), style = MaterialTheme.typography.labelLarge)
                            Text(site.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
