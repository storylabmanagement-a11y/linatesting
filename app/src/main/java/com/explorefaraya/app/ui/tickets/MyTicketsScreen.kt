package com.explorefaraya.app.ui.tickets

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
import com.explorefaraya.app.data.model.Booking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTicketsScreen(
    onTicketClick: (String) -> Unit,
    viewModel: TicketsViewModel = viewModel()
) {
    val bookings by viewModel.bookings.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Tickets") }) }
    ) { padding ->
        if (bookings.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "No tickets yet. Book an event from the Events tab to see it here.",
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
                items(bookings, key = { it.id }) { booking ->
                    BookingSummaryCard(booking = booking, onClick = { onTicketClick(booking.id) })
                }
            }
        }
    }
}

@Composable
private fun BookingSummaryCard(booking: Booking, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(booking.eventTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(booking.eventDate, style = MaterialTheme.typography.bodyMedium)
            Text(
                "${booking.quantity} ticket(s) · $${"%.2f".format(booking.totalPrice)} · #${booking.ticketNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
