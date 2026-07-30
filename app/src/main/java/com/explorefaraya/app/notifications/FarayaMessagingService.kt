package com.explorefaraya.app.notifications

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService

/**
 * Registers the device's FCM token to the signed-in user's Firestore doc. Actual push sends
 * (event drops, early-bird windows, news) can be triggered manually from the Firebase Console's
 * Cloud Messaging composer once tokens are registered — no extra backend code required for that.
 */
class FarayaMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("users").document(uid)
            .set(mapOf("fcmToken" to token), SetOptions.merge())
    }
}
