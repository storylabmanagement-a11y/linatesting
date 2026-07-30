package com.explorefaraya.app.ui.subscription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.explorefaraya.app.ui.common.FarayaButton
import com.explorefaraya.app.ui.common.FarayaTextField
import com.explorefaraya.app.ui.theme.FBGold
import com.explorefaraya.app.ui.theme.FBSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(onBack: () -> Unit, viewModel: SubscriptionViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTier by remember { mutableStateOf("Monthly") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Faraya & Beyond Insider") },
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
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp)
        ) {
            if (uiState.isPremium) {
                Row {
                    Icon(Icons.Default.Star, contentDescription = null, tint = FBGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("You're an Insider member (${uiState.tier})", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Enjoy early-bird ticket drops, exclusive events, discounted pricing, and a seasonal guide every 3 months.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(onClick = { viewModel.cancel() }) { Text("Cancel membership") }
                return@Column
            }

            Text("Go Insider", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            listOf(
                "Early-bird access to event ticket drops before public release",
                "One curated seasonal guide delivered every 3 months",
                "Exclusive access to select tickets and invite-only events",
                "Discounted pricing on tickets and partner venues",
                "Gold member badge on your profile"
            ).forEach {
                Text("• $it", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TierCard("Monthly", "$10/mo", selectedTier == "Monthly", Modifier.weight(1f)) { selectedTier = "Monthly" }
                TierCard("Annual", "$100/yr — 2 months free", selectedTier == "Annual", Modifier.weight(1f)) { selectedTier = "Annual" }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Demo payment — no real recurring charge will be set up", style = MaterialTheme.typography.bodyMedium)
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
                text = "Become an Insider",
                isLoading = uiState.isProcessing,
                onClick = { viewModel.upgrade(selectedTier, cardNumber, expiry, cvv) {} }
            )
        }
    }
}

@Composable
private fun TierCard(name: String, price: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = if (selected) FBGold.copy(alpha = 0.15f) else FBSurface),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Text(price, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
