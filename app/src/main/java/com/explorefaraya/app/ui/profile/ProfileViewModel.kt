package com.explorefaraya.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val bio: String = "",
    val isPremium: Boolean = false,
    val premiumTier: String = "",
    val notifyEventDrops: Boolean = true,
    val notifyEarlyBird: Boolean = true,
    val notifyNews: Boolean = false,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val errorMessage: String? = null
)

class ProfileViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        val user = auth.currentUser
        _uiState.value = ProfileUiState(
            name = user?.displayName.orEmpty(),
            email = user?.email.orEmpty(),
            isLoading = true
        )
        loadProfile()
    }

    private fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users").document(uid).get().await()
                _uiState.value = _uiState.value.copy(
                    phone = doc.getString("phone").orEmpty(),
                    bio = doc.getString("bio").orEmpty(),
                    isPremium = doc.getBoolean("isPremium") ?: false,
                    premiumTier = doc.getString("premiumTier").orEmpty(),
                    notifyEventDrops = doc.getBoolean("notifyEventDrops") ?: true,
                    notifyEarlyBird = doc.getBoolean("notifyEarlyBird") ?: true,
                    notifyNews = doc.getBoolean("notifyNews") ?: false,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun save(name: String, phone: String, bio: String) {
        val user = auth.currentUser ?: return
        _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null, saved = false)
        viewModelScope.launch {
            try {
                user.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(name.trim()).build()
                ).await()
                firestore.collection("users").document(user.uid)
                    .set(mapOf("phone" to phone.trim(), "bio" to bio.trim()), SetOptions.merge())
                    .await()
                _uiState.value = _uiState.value.copy(
                    name = name.trim(),
                    phone = phone.trim(),
                    bio = bio.trim(),
                    isSaving = false,
                    saved = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = e.message ?: "Could not save profile."
                )
            }
        }
    }

    fun setNotificationPref(key: String, value: Boolean) {
        val uid = auth.currentUser?.uid ?: return
        _uiState.value = when (key) {
            "notifyEventDrops" -> _uiState.value.copy(notifyEventDrops = value)
            "notifyEarlyBird" -> _uiState.value.copy(notifyEarlyBird = value)
            else -> _uiState.value.copy(notifyNews = value)
        }
        viewModelScope.launch {
            firestore.collection("users").document(uid).set(mapOf(key to value), SetOptions.merge()).await()
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
