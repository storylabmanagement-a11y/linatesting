package com.explorefaraya.app.ui.explore

import java.net.URLEncoder

/** Derives contact/navigation actions from a listing's raw phone number and "Link Type" text. */
object ListingActions {

    fun primaryPhone(phone: String): String? {
        if (phone.isBlank()) return null
        // Some rows list two numbers separated by "/" — use the first.
        return phone.split("/").first().trim().takeIf { it.isNotBlank() }
    }

    fun telUri(phone: String): String = "tel:${phone.replace(" ", "")}"

    fun whatsAppUri(phone: String): String {
        val digits = phone.filter { it.isDigit() }
        return "https://wa.me/$digits"
    }

    fun mapsSearchUri(name: String): String {
        val query = URLEncoder.encode("$name Faraya Lebanon", "UTF-8")
        return "https://www.google.com/maps/search/?api=1&query=$query"
    }

    fun hasWhatsApp(linkType: String): Boolean = linkType.contains("WhatsApp", ignoreCase = true)

    fun hasDirections(linkType: String): Boolean = linkType.contains("Location", ignoreCase = true)

    fun websiteUrl(linkType: String): String? {
        val match = Regex("Website\\s*\\(([^)]+)\\)").find(linkType) ?: return null
        val domain = match.groupValues[1].trim()
        return if (domain.startsWith("http")) domain else "https://$domain"
    }

    fun instagramUrl(linkType: String): String? {
        val match = Regex("Instagram\\s*\\(@?([^)]+)\\)").find(linkType) ?: return null
        val handle = match.groupValues[1].trim().removePrefix("@")
        return "https://instagram.com/$handle"
    }

    fun facebookUrl(linkType: String): String? {
        if (!linkType.contains("Facebook", ignoreCase = true)) return null
        val match = Regex("Facebook\\s*\\(([^)]+)\\)").find(linkType)
        return match?.groupValues?.get(1)?.trim()
    }
}
