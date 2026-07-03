package com.explorefaraya.app.data.repository

import com.explorefaraya.app.data.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ReservationRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private fun reservationsCollection() =
        firestore.collection("users").document(requireUid()).collection("reservations")

    private fun requireUid(): String =
        auth.currentUser?.uid ?: error("No authenticated user")

    fun observeReservations(): Flow<List<Reservation>> = callbackFlow {
        val registration = reservationsCollection()
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val reservations = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Reservation::class.java)?.copy(id = doc.id)
                }.orEmpty().sortedByDescending { it.createdAt }
                trySend(reservations)
            }
        awaitClose { registration.remove() }
    }

    suspend fun getReservation(reservationId: String): Reservation? {
        val doc = reservationsCollection().document(reservationId).get().await()
        return doc.toObject(Reservation::class.java)?.copy(id = doc.id)
    }

    suspend fun createReservation(
        listingId: String,
        listingTitle: String,
        category: String,
        contact: String,
        scheduledFor: String,
        partySize: Int,
        unitPrice: Double,
        totalPrice: Double
    ): String {
        val confirmationNumber = "EF-" + UUID.randomUUID().toString().take(8).uppercase()
        val reservation = hashMapOf(
            "listingId" to listingId,
            "listingTitle" to listingTitle,
            "category" to category,
            "contact" to contact,
            "scheduledFor" to scheduledFor,
            "partySize" to partySize,
            "unitPrice" to unitPrice,
            "totalPrice" to totalPrice,
            "confirmationNumber" to confirmationNumber,
            "createdAt" to System.currentTimeMillis(),
            "status" to "CONFIRMED"
        )
        val ref = reservationsCollection().add(reservation).await()
        return ref.id
    }
}
