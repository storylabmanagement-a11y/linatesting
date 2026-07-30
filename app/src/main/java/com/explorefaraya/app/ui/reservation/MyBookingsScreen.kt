package com.explorefaraya.app.ui.reservation

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
import com.explorefaraya.app.data.model.Reservation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    onBookingClick: (String) -> Unit,
    viewModel: ReservationsViewModel = viewModel()
) {
    val reservations by viewModel.reservations.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Bookings") }) }
    ) { padding ->
        if (reservations.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "No bookings yet. Book a restaurant, chalet, activity or ride from the Explore tab to see it here.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(reservations, key = { it.id }) { reservation ->
                    BookingSummaryCard(reservation = reservation, onClick = { onBookingClick(reservation.id) })
                }
            }
        }
    }
}

@Composable
private fun BookingSummaryCard(reservation: Reservation, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(reservation.listingTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(reservation.category, style = MaterialTheme.typography.bodyMedium)
            Text(reservation.scheduledFor, style = MaterialTheme.typography.bodyMedium)
            val tierSuffix = if (reservation.tierName.isNotBlank()) " · ${reservation.tierName}" else ""
            Text(
                "${reservation.partySize} unit(s)$tierSuffix · #${reservation.confirmationNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
