package com.explorefaraya.app.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explorefaraya.app.data.model.Booking
import com.explorefaraya.app.data.model.EventItem
import com.explorefaraya.app.data.repository.BookingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PaymentUiState(
    val isProcessing: Boolean = false,
    val errorMessage: String? = null
)

class BookingViewModel(
    private val repository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    private val _selectedBooking = MutableStateFlow<Booking?>(null)
    val selectedBooking: StateFlow<Booking?> = _selectedBooking.asStateFlow()

    fun loadBooking(bookingId: String) {
        viewModelScope.launch {
            _selectedBooking.value = repository.getBooking(bookingId)
        }
    }

    fun pay(
        event: EventItem,
        quantity: Int,
        cardNumber: String,
        expiry: String,
        cvv: String,
        onSuccess: (bookingId: String) -> Unit
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

        _uiState.value = PaymentUiState(isProcessing = true)
        viewModelScope.launch {
            // Simulated payment authorization delay (no real charge, no gateway wired up).
            delay(1400)
            try {
                val bookingId = repository.createBooking(
                    eventId = event.id,
                    eventTitle = event.title,
                    eventDate = event.date,
                    eventLocation = event.location,
                    quantity = quantity,
                    unitPrice = event.price
                )
                _uiState.value = PaymentUiState()
                onSuccess(bookingId)
            } catch (e: Exception) {
                _uiState.value = PaymentUiState(errorMessage = e.message ?: "Payment failed. Please try again.")
            }
        }
    }
}
