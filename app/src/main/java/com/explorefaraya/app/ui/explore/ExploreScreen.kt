package com.explorefaraya.app.ui.explore

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.explorefaraya.app.data.model.ExploreCatalog
import com.explorefaraya.app.data.model.SiteCategory
import com.explorefaraya.app.data.model.TouristSite
import com.explorefaraya.app.ui.saved.BookmarksViewModel
import com.explorefaraya.app.ui.theme.FBCard
import com.explorefaraya.app.ui.theme.FBGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(onSiteClick: (String) -> Unit, bookmarksViewModel: BookmarksViewModel = viewModel()) {
    var selectedCategory by remember { mutableStateOf<SiteCategory?>(null) }
    val sites = selectedCategory?.let { ExploreCatalog.byCategory(it) } ?: ExploreCatalog.sites
    val bookmarks by bookmarksViewModel.bookmarks.collectAsState()

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
                    SiteCard(
                        site = site,
                        isBookmarked = site.id in bookmarks.siteIds,
                        onClick = { onSiteClick(site.id) },
                        onBookmarkClick = { bookmarksViewModel.toggleSite(site.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SiteCard(site: TouristSite, isBookmarked: Boolean, onClick: () -> Unit, onBookmarkClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = FBCard)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                val photo = site.photos.firstOrNull()
                if (!photo.isNullOrBlank()) {
                    AsyncImage(
                        model = photo,
                        contentDescription = site.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Terrain, contentDescription = null, tint = FBGold)
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(FBGold)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        site.category.label.lowercase(),
                        style = MaterialTheme.typography.labelLarge.copy(color = androidx.compose.ui.graphics.Color.Black)
                    )
                }
                IconButton(onClick = onBookmarkClick, modifier = Modifier.align(Alignment.TopEnd)) {
                    Icon(
                        if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(site.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    site.shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 3.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.height(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            " ${site.driveTimeBeirut} from Beirut",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ListingActions.mapsSearchUri(site.name))))
                        }
                    ) {
                        Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.height(14.dp), tint = FBGold)
                        Text(" Directions", style = MaterialTheme.typography.bodyMedium.copy(color = FBGold))
                    }
                }
            }
        }
    }
}
