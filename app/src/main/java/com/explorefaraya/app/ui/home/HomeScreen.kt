package com.explorefaraya.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.explorefaraya.app.data.model.EventCatalog
import com.explorefaraya.app.data.model.ExploreCatalog
import com.explorefaraya.app.data.model.NewsCatalog
import com.explorefaraya.app.data.model.NewsPost
import com.explorefaraya.app.ui.theme.FBGold
import com.explorefaraya.app.ui.theme.FBCard
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

private fun greeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour < 12 -> "Good morning"
        hour < 18 -> "Good afternoon"
        else -> "Good evening"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onExploreClick: () -> Unit,
    onEventsClick: () -> Unit,
    onPremiumClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSiteClick: (String) -> Unit,
    onEventClick: (String) -> Unit
) {
    val displayName = FirebaseAuth.getInstance().currentUser?.displayName
        ?.split(" ")?.firstOrNull()?.takeIf { it.isNotBlank() } ?: "Explorer"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(greeting(), style = MaterialTheme.typography.bodyMedium)
                        Text(displayName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = FBGold, modifier = Modifier.size(18.dp))
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FBCard)
                        .clickable(onClick = onSearchClick)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = FBGold)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Search places & events", style = MaterialTheme.typography.bodyLarge)
                }
            }

            item {
                QuickLinksRow(onExploreClick, onEventsClick, onPremiumClick)
            }

            item {
                Text(
                    "Featured",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )
                FeaturedCarousel(onSiteClick = onSiteClick, onEventClick = onEventClick)
            }

            item {
                Text(
                    "Latest",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
                )
            }

            items(NewsCatalog.posts) { post ->
                NewsCard(post)
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun QuickLinksRow(onExploreClick: () -> Unit, onEventsClick: () -> Unit, onPremiumClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuickLinkChip("Explore", Icons.Default.Explore, Modifier.weight(1f), onExploreClick)
        QuickLinkChip("Events", Icons.Default.CalendarMonth, Modifier.weight(1f), onEventsClick)
        QuickLinkChip("Go Premium", Icons.Default.Star, Modifier.weight(1f), onPremiumClick)
    }
}

@Composable
private fun QuickLinkChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(FBCard)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = FBGold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun FeaturedCarousel(onSiteClick: (String) -> Unit, onEventClick: (String) -> Unit) {
    val featuredSites = ExploreCatalog.featured()
    val featuredEvents = EventCatalog.featured()

    LazyRow(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(featuredEvents) { event ->
            FeaturedCard(title = event.title, subtitle = event.venue, imageUrl = null, onClick = { onEventClick(event.id) })
        }
        items(featuredSites) { site ->
            FeaturedCard(title = site.name, subtitle = site.category.label, imageUrl = site.photos.firstOrNull(), onClick = { onSiteClick(site.id) })
        }
    }
}

@Composable
private fun FeaturedCard(title: String, subtitle: String, imageUrl: String?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(FBCard)
            .clickable(onClick = onClick)
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(FBGold)
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text("FEATURED", style = MaterialTheme.typography.labelLarge.copy(color = androidx.compose.ui.graphics.Color.Black))
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.55f))
                .padding(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
        }
    }
}

@Composable
private fun NewsCard(post: NewsPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = FBCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(post.category.uppercase(), style = MaterialTheme.typography.labelLarge)
            Text(post.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(post.blurb, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
