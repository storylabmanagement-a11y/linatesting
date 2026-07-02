package com.explorefaraya.app.data.repository

import com.explorefaraya.app.data.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class BookingRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private fun bookingsCollection() =
        firestore.collection("users").document(requireUid()).collection("bookings")

    private fun requireUid(): String =
        auth.currentUser?.uid ?: error("No authenticated user")

    fun observeBookings(): Flow<List<Booking>> = callbackFlow {
        val registration = bookingsCollection()
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val bookings = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Booking::class.java)?.copy(id = doc.id)
                }.orEmpty().sortedByDescending { it.purchasedAt }
                trySend(bookings)
            }
        awaitClose { registration.remove() }
    }

    suspend fun getBooking(bookingId: String): Booking? {
        val doc = bookingsCollection().document(bookingId).get().await()
        return doc.toObject(Booking::class.java)?.copy(id = doc.id)
    }

    suspend fun createBooking(
        eventId: String,
        eventTitle: String,
        eventDate: String,
        eventLocation: String,
        quantity: Int,
        unitPrice: Double
    ): String {
        val ticketNumber = "EF-" + UUID.randomUUID().toString().take(8).uppercase()
        val booking = hashMapOf(
            "eventId" to eventId,
            "eventTitle" to eventTitle,
            "eventDate" to eventDate,
            "eventLocation" to eventLocation,
            "quantity" to quantity,
            "unitPrice" to unitPrice,
            "totalPrice" to unitPrice * quantity,
            "ticketNumber" to ticketNumber,
            "purchasedAt" to System.currentTimeMillis(),
            "status" to "PAID"
        )
        val ref = bookingsCollection().add(booking).await()
        return ref.id
    }
}
