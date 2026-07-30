package com.explorefaraya.app.ui.reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.explorefaraya.app.data.model.EventCatalog
import com.explorefaraya.app.ui.common.FarayaButton
import com.explorefaraya.app.ui.common.FarayaTextField
import com.explorefaraya.app.ui.theme.FBGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCheckoutScreen(
    eventId: String,
    tierName: String,
    quantity: Int,
    onBack: () -> Unit,
    onPaymentSuccess: (reservationId: String) -> Unit,
    viewModel: ReservationViewModel = viewModel()
) {
    val event = EventCatalog.findById(eventId) ?: return
    val tier = event.tiers.firstOrNull { it.name == tierName } ?: return
    val uiState by viewModel.uiState.collectAsState()

    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val total = tier.price * quantity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 1.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(event.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(event.date, style = MaterialTheme.typography.bodyMedium)
                    Text("${tier.name} × $quantity", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Total: $${"%.2f".format(total)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = FBGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Demo checkout — no real charge will be made",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            FarayaTextField(value = cardName, onValueChange = { cardName = it }, label = "Name on card")
            Spacer(modifier = Modifier.height(12.dp))
            FarayaTextField(
                value = cardNumber,
                onValueChange = { if (it.length <= 19) cardNumber = it },
                label = "Card number",
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                FarayaTextField(
                    value = expiry,
                    onValueChange = { if (it.length <= 5) expiry = it },
                    label = "MM/YY",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                FarayaTextField(
                    value = cvv,
                    onValueChange = { if (it.length <= 4) cvv = it },
                    label = "CVV",
                    keyboardType = KeyboardType.NumberPassword,
                    modifier = Modifier.weight(1f)
                )
            }

            uiState.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))
            FarayaButton(
                text = "Pay $${"%.2f".format(total)}",
                isLoading = uiState.isProcessing,
                onClick = {
                    viewModel.buyEventTickets(event, tier, quantity, cardNumber, expiry, cvv) { reservationId ->
                        onPaymentSuccess(reservationId)
                    }
                }
            )
        }
    }
}
