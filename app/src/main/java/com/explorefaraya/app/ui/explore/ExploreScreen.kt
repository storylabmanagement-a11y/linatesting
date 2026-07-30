package com.explorefaraya.app.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.explorefaraya.app.data.model.ExploreCatalog
import com.explorefaraya.app.data.model.SiteCategory
import com.explorefaraya.app.data.model.TouristSite
import com.explorefaraya.app.ui.theme.FBGold
import com.explorefaraya.app.ui.theme.FBSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(onSiteClick: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf<SiteCategory?>(null) }
    val sites = selectedCategory?.let { ExploreCatalog.byCategory(it) } ?: ExploreCatalog.sites

    Scaffold(
        topBar = { TopAppBar(title = { Text("Explore") }) }
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
                items(SiteCategory.entries) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = if (selectedCategory == category) null else category },
                        label = { Text(category.label) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sites, key = { it.id }) { site ->
                    SiteCard(site = site, onClick = { onSiteClick(site.id) })
                }
            }
        }
    }
}

@Composable
private fun SiteCard(site: TouristSite, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = FBSurface)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            val photo = site.photos.firstOrNull()
            if (!photo.isNullOrBlank()) {
                AsyncImage(
                    model = photo,
                    contentDescription = site.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(72.dp).clip(RoundedCornerShape(10.dp))
                )
            } else {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Terrain, contentDescription = null, tint = FBGold)
                }
            }
            Column(modifier = Modifier.padding(start = 12.dp).fillMaxWidth()) {
                Text(site.category.label.uppercase(), style = MaterialTheme.typography.labelLarge)
                Text(site.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(site.shortDescription, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            }
        }
    }
}
