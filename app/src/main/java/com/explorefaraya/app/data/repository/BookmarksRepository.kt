package com.explorefaraya.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class BookmarksRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private fun doc() = firestore.collection("users").document(requireUid())
        .collection("prefs").document("bookmarks")

    private fun requireUid(): String = auth.currentUser?.uid ?: error("No authenticated user")

    fun observeBookmarkedEventIds(): Flow<Set<String>> = callbackFlow {
        val registration = doc().addSnapshotListener { snapshot, _ ->
            @Suppress("UNCHECKED_CAST")
            val ids = (snapshot?.get("eventIds") as? List<String>)?.toSet() ?: emptySet()
            trySend(ids)
        }
        awaitClose { registration.remove() }
    }

    suspend fun toggleBookmark(eventId: String, isBookmarked: Boolean) {
        val update = if (isBookmarked) {
            mapOf("eventIds" to FieldValue.arrayRemove(eventId))
        } else {
            mapOf("eventIds" to FieldValue.arrayUnion(eventId))
        }
        doc().set(update, com.google.firebase.firestore.SetOptions.merge()).await()
    }
}
