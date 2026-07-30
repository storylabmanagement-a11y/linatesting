package com.explorefaraya.app.ui.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SubscriptionUiState(
    val isPremium: Boolean = false,
    val tier: String = "",
    val isProcessing: Boolean = false,
    val errorMessage: String? = null
)

class SubscriptionViewModel(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubscriptionUiState())
    val uiState: StateFlow<SubscriptionUiState> = _uiState.asStateFlow()

    init {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                try {
                    val doc = firestore.collection("users").document(uid).get().await()
                    _uiState.value = _uiState.value.copy(
                        isPremium = doc.getBoolean("isPremium") ?: false,
                        tier = doc.getString("premiumTier").orEmpty()
                    )
                } catch (_: Exception) { }
            }
        }
    }

    fun upgrade(tier: String, cardNumber: String, expiry: String, cvv: String, onSuccess: () -> Unit) {
        val digitsOnly = cardNumber.filter { it.isDigit() }
        if (digitsOnly.length < 12) {
            _uiState.value = _uiState.value.copy(errorMessage = "Enter a valid card number."); return
        }
        if (!expiry.matches(Regex("^\\d{2}/\\d{2}$"))) {
            _uiState.value = _uiState.value.copy(errorMessage = "Enter expiry as MM/YY."); return
        }
        if (cvv.length < 3) {
            _uiState.value = _uiState.value.copy(errorMessage = "Enter a valid CVV."); return
        }
        val uid = auth.currentUser?.uid ?: return
        _uiState.value = _uiState.value.copy(isProcessing = true, errorMessage = null)
        viewModelScope.launch {
            delay(1400) // simulated authorization delay — no real recurring billing wired up
            try {
                firestore.collection("users").document(uid).set(
                    mapOf(
                        "isPremium" to true,
                        "premiumTier" to tier,
                        "premiumSince" to System.currentTimeMillis()
                    ),
                    SetOptions.merge()
                ).await()
                _uiState.value = SubscriptionUiState(isPremium = true, tier = tier)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isProcessing = false, errorMessage = e.message ?: "Something went wrong.")
            }
        }
    }

    fun cancel() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            firestore.collection("users").document(uid)
                .set(mapOf("isPremium" to false), SetOptions.merge()).await()
            _uiState.value = _uiState.value.copy(isPremium = false, tier = "")
        }
    }
}
