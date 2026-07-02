package com.explorefaraya.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
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
        loadPhone()
    }

    private fun loadPhone() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users").document(uid).get().await()
                val phone = doc.getString("phone").orEmpty()
                _uiState.value = _uiState.value.copy(phone = phone, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun save(name: String, phone: String) {
        val user = auth.currentUser ?: return
        _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null, saved = false)
        viewModelScope.launch {
            try {
                user.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(name.trim()).build()
                ).await()
                firestore.collection("users").document(user.uid)
                    .set(mapOf("phone" to phone.trim()), com.google.firebase.firestore.SetOptions.merge())
                    .await()
                _uiState.value = _uiState.value.copy(
                    name = name.trim(),
                    phone = phone.trim(),
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

    fun signOut() {
        auth.signOut()
    }
}
