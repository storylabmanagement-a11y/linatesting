package com.explorefaraya.app.ui.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explorefaraya.app.data.model.ExploreListing
import com.explorefaraya.app.data.model.Reservation
import com.explorefaraya.app.data.repository.ReservationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PaymentUiState(
    val isProcessing: Boolean = false,
    val errorMessage: String? = null
)

class ReservationViewModel(
    private val repository: ReservationRepository = ReservationRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    private val _selectedReservation = MutableStateFlow<Reservation?>(null)
    val selectedReservation: StateFlow<Reservation?> = _selectedReservation.asStateFlow()

    fun loadReservation(reservationId: String) {
        viewModelScope.launch {
            _selectedReservation.value = repository.getReservation(reservationId)
        }
    }

    fun pay(
        listing: ExploreListing,
        partySize: Int,
        scheduledFor: String,
        cardNumber: String,
        expiry: String,
        cvv: String,
        onSuccess: (reservationId: String) -> Unit
    ) {
        val digitsOnly = cardNumber.filter { it.isDigit() }
        if (digitsOnly.length < 12) {
            _uiState.value = PaymentUiState(errorMessage = "Enter a valid card number.")
            return
        }
        if (!expiry.matches(Regex("^\\d{2}/\\d{2}$"))) {
            _uiState.value = PaymentUiState(errorMessage = "Enter expiry as MM/YY.")
            return
        }
        if (cvv.length < 3) {
            _uiState.value = PaymentUiState(errorMessage = "Enter a valid CVV.")
            return
        }
        if (scheduledFor.isBlank()) {
            _uiState.value = PaymentUiState(errorMessage = "Enter a preferred date/time.")
            return
        }

        // Real listings are a contact directory, not a priced inventory — there's no fare/rate
        // to charge, so this confirms the reservation request without a monetary total.
        _uiState.value = PaymentUiState(isProcessing = true)
        viewModelScope.launch {
            // Simulated payment authorization delay (no real charge, no gateway wired up).
            delay(1400)
            try {
                val reservationId = repository.createReservation(
                    listingId = listing.id,
                    listingTitle = listing.name,
                    category = listing.category,
                    contact = listing.phone,
                    scheduledFor = scheduledFor,
                    partySize = partySize,
                    unitPrice = 0.0,
                    totalPrice = 0.0
                )
                _uiState.value = PaymentUiState()
                onSuccess(reservationId)
            } catch (e: Exception) {
                _uiState.value = PaymentUiState(errorMessage = e.message ?: "Payment failed. Please try again.")
            }
        }
    }
}
