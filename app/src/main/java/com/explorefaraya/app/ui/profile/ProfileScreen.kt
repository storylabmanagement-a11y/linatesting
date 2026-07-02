package com.explorefaraya.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.explorefaraya.app.ui.common.FarayaButton
import com.explorefaraya.app.ui.common.FarayaTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignedOut: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            name = uiState.name
            phone = uiState.phone
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
            AvatarInitials(name = name.ifBlank { uiState.email })
            Spacer(modifier = Modifier.height(24.dp))

            FarayaTextField(value = name, onValueChange = { name = it }, label = "Full name")
            Spacer(modifier = Modifier.height(12.dp))
            FarayaTextField(
                value = uiState.email,
                onValueChange = {},
                label = "Email",
                keyboardType = KeyboardType.Email,
                readOnly = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            FarayaTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone number",
                keyboardType = KeyboardType.Phone
            )

            uiState.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
            if (uiState.saved) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Profile saved",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            FarayaButton(
                text = "Save Changes",
                isLoading = uiState.isSaving,
                onClick = { viewModel.save(name, phone) }
            )

            Spacer(modifier = Modifier.height(32.dp))
            FarayaButton(
                text = "Log Out",
                onClick = {
                    viewModel.signOut()
                    onSignedOut()
                }
            )
        }
    }
}

@Composable
private fun AvatarInitials(name: String) {
    val initial = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "E"
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initial,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
    }
}
