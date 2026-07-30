package com.explorefaraya.app.ui.profile

import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.explorefaraya.app.ui.common.FarayaButton
import com.explorefaraya.app.ui.common.FarayaTextField
import com.explorefaraya.app.ui.theme.FBGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignedOut: () -> Unit,
    onSubscriptionClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("en") }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            name = uiState.name
            phone = uiState.phone
            bio = uiState.bio
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarInitials(name.ifBlank { uiState.email })
            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.isPremium) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = FBGold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Insider Member", color = FBGold, fontWeight = FontWeight.Bold)
                }
            } else {
                Row(
                    modifier = Modifier.clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                        .background(FBGold.copy(alpha = 0.15f))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text("Upgrade to Insider", color = FBGold, modifier = Modifier.clickable(onClick = onSubscriptionClick))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            FarayaTextField(value = name, onValueChange = { name = it }, label = "Full name")
            Spacer(modifier = Modifier.height(12.dp))
            FarayaTextField(value = uiState.email, onValueChange = {}, label = "Email", keyboardType = KeyboardType.Email, readOnly = true)
            Spacer(modifier = Modifier.height(12.dp))
            FarayaTextField(value = phone, onValueChange = { phone = it }, label = "Phone number", keyboardType = KeyboardType.Phone)
            Spacer(modifier = Modifier.height(12.dp))
            FarayaTextField(value = bio, onValueChange = { bio = it }, label = "Bio", singleLine = false)

            uiState.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            if (uiState.saved) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Profile saved", color = FBGold)
            }

            Spacer(modifier = Modifier.height(16.dp))
            FarayaButton(text = "Save Changes", isLoading = uiState.isSaving, onClick = { viewModel.save(name, phone, bio) })

            Spacer(modifier = Modifier.height(28.dp))
            Text("Notifications", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            NotificationToggleRow("Event drops", uiState.notifyEventDrops) { viewModel.setNotificationPref("notifyEventDrops", it) }
            NotificationToggleRow("Early-bird access", uiState.notifyEarlyBird) { viewModel.setNotificationPref("notifyEarlyBird", it) }
            NotificationToggleRow("News updates", uiState.notifyNews) { viewModel.setNotificationPref("notifyNews", it) }

            Spacer(modifier = Modifier.height(28.dp))
            Text("Language", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("en" to "English", "ar" to "العربية", "fr" to "Français").forEach { (code, label) ->
                    FilterChip(
                        selected = selectedLanguage == code,
                        onClick = {
                            selectedLanguage = code
                            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(code))
                        },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            FarayaButton(text = "Log Out", onClick = { viewModel.signOut(); onSignedOut() })
        }
    }
}

@Composable
private fun NotificationToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun AvatarInitials(name: String) {
    val initial = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "F"
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(FBGold),
        contentAlignment = Alignment.Center
    ) {
        Text(initial, color = androidx.compose.ui.graphics.Color.Black, fontWeight = FontWeight.Bold, fontSize = 36.sp)
    }
}
