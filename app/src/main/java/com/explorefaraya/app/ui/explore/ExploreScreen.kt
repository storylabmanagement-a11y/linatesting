package com.explorefaraya.app.ui.explore

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.explorefaraya.app.data.model.ExploreCatalog
import com.explorefaraya.app.data.model.ExploreCategory
import com.explorefaraya.app.data.model.ExploreListing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(onListingClick: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf<ExploreCategory?>(null) }

    val listings = selectedCategory?.let { ExploreCatalog.byCategory(it) } ?: ExploreCatalog.listings

    Scaffold(
        topBar = { TopAppBar(title = { Text("Explore Faraya") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All") }
                    )
                }
                items(ExploreCategory.entries) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                        },
                        label = { Text(category.label) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listings, key = { it.id }) { listing ->
                    ExploreListingCard(listing = listing, onClick = { onListingClick(listing.id) })
                }
            }
        }
    }
}

@Composable
private fun ExploreListingCard(listing: ExploreListing, onClick: () -> Unit) {
    val accent = Color(android.graphics.Color.parseColor(listing.accentColorHex))
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxWidth().height(8.dp).background(accent)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(listing.category.label.uppercase(), style = MaterialTheme.typography.labelLarge, color = accent)
                Text(listing.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(listing.location, style = MaterialTheme.typography.bodyMedium)
                if (listing.price > 0) {
                    Text(
                        "$${"%.2f".format(listing.price)} ${listing.priceUnit}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
