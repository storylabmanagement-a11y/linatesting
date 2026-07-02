package com.explorefaraya.app.data.repository

import com.explorefaraya.app.data.model.DashboardItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DashboardRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private fun itemsCollection() =
        firestore.collection("users").document(requireUid()).collection("dashboard_items")

    private fun requireUid(): String =
        auth.currentUser?.uid ?: error("No authenticated user")

    fun observeItems(): Flow<List<DashboardItem>> = callbackFlow {
        val registration = itemsCollection()
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(DashboardItem::class.java)?.copy(id = doc.id)
                }.orEmpty().sortedByDescending { it.createdAt }
                trySend(items)
            }
        awaitClose { registration.remove() }
    }

    suspend fun addItem(title: String, note: String) {
        val item = hashMapOf(
            "title" to title,
            "note" to note,
            "createdAt" to System.currentTimeMillis()
        )
        itemsCollection().add(item).await()
    }

    suspend fun deleteItem(itemId: String) {
        itemsCollection().document(itemId).delete().await()
    }
}
