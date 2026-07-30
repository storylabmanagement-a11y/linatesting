package com.explorefaraya.app.ui.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explorefaraya.app.data.model.EventItem
import com.explorefaraya.app.data.model.Reservation
import com.explorefaraya.app.data.model.TicketTier
import com.explorefaraya.app.data.model.TouristSite
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

    private fun validateCard(cardNumber: String, expiry: String, cvv: String): String? {
        val digitsOnly = cardNumber.filter { it.isDigit() }
        if (digitsOnly.length < 12) return "Enter a valid card number."
        if (!expiry.matches(Regex("^\\d{2}/\\d{2}$"))) return "Enter expiry as MM/YY."
        if (cvv.length < 3) return "Enter a valid CVV."
        return null
    }

    fun reserveSite(
        site: TouristSite,
        partySize: Int,
        scheduledFor: String,
        cardNumber: String,
        expiry: String,
        cvv: String,
        onSuccess: (reservationId: String) -> Unit
    ) {
        validateCard(cardNumber, expiry, cvv)?.let {
            _uiState.value = PaymentUiState(errorMessage = it); return
        }
        if (scheduledFor.isBlank()) {
            _uiState.value = PaymentUiState(errorMessage = "Enter a preferred date/time.")
            return
        }
        _uiState.value = PaymentUiState(isProcessing = true)
        viewModelScope.launch {
            delay(1400) // simulated authorization delay — no real charge, no gateway wired up
            try {
                val id = repository.createReservation(
                    type = "listing",
                    listingId = site.id,
                    listingTitle = site.name,
                    category = site.category.label,
                    contact = site.phone,
                    scheduledFor = scheduledFor,
                    partySize = partySize,
                    unitPrice = 0.0,
                    totalPrice = 0.0
                )
                _uiState.value = PaymentUiState()
                onSuccess(id)
            } catch (e: Exception) {
                _uiState.value = PaymentUiState(errorMessage = e.message ?: "Something went wrong. Please try again.")
            }
        }
    }

    fun buyEventTickets(
        event: EventItem,
        tier: TicketTier,
        quantity: Int,
        cardNumber: String,
        expiry: String,
        cvv: String,
        onSuccess: (reservationId: String) -> Unit
    ) {
        validateCard(cardNumber, expiry, cvv)?.let {
            _uiState.value = PaymentUiState(errorMessage = it); return
        }
        _uiState.value = PaymentUiState(isProcessing = true)
        viewModelScope.launch {
            delay(1400) // simulated authorization delay — no real charge, no gateway wired up
            try {
                val id = repository.createReservation(
                    type = "event",
                    listingId = event.id,
                    listingTitle = event.title,
                    category = event.category,
                    contact = event.venue,
                    scheduledFor = event.date,
                    tierName = tier.name,
                    partySize = quantity,
                    unitPrice = tier.price,
                    totalPrice = tier.price * quantity
                )
                _uiState.value = PaymentUiState()
                onSuccess(id)
            } catch (e: Exception) {
                _uiState.value = PaymentUiState(errorMessage = e.message ?: "Something went wrong. Please try again.")
            }
        }
    }
}
