package com.explorefaraya.app.data.model

/** A news/editorial post on the Home feed. */
data class NewsPost(
    val id: String,
    val title: String,
    val blurb: String,
    val imageUrl: String,
    val category: String
)

enum class SiteCategory(val label: String) {
    NATURE("Nature"),
    SKI("Ski & Snow"),
    ADVENTURE("Adventure"),
    DINING("Dining"),
    WELLNESS("Wellness"),
    FAMILY("Family"),
    NIGHTLIFE("Nightlife"),
    CULTURAL("Cultural")
}

/** A curated touristic site/venue in the Faraya & Beyond region. */
data class TouristSite(
    val id: String,
    val name: String,
    val category: SiteCategory,
    val shortDescription: String,
    val longDescription: String,
    val photos: List<String>,
    val hours: String,
    val entryFee: String,
    val phone: String,
    val driveTimeBeirut: String,
    val driveTimeJounieh: String,
    val featured: Boolean = false,
    val bookable: Boolean = false
)

data class TicketTier(
    val name: String,
    val price: Double,
    val description: String = ""
)

/** A flagship or calendar event in the region. */
data class EventItem(
    val id: String,
    val title: String,
    val organizer: String,
    val venue: String,
    val date: String,
    val dateTimeMillis: Long,
    val description: String,
    val lineup: List<String>,
    val photos: List<String>,
    val category: String,
    val tiers: List<TicketTier>,
    val featured: Boolean = false,
    val insiderEarlyAccessUntilMillis: Long = 0L
)

enum class ReservationStatus { CONFIRMED, CANCELLED }

/**
 * A confirmed booking — either a directory listing reservation (type = "listing")
 * or an event ticket purchase (type = "event"), unified so the Bookings wallet
 * doesn't need two separate models.
 */
data class Reservation(
    val id: String = "",
    val type: String = "listing",
    val listingId: String = "",
    val listingTitle: String = "",
    val category: String = "",
    val contact: String = "",
    val scheduledFor: String = "",
    val tierName: String = "",
    val partySize: Int = 1,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val confirmationNumber: String = "",
    val createdAt: Long = 0L,
    val status: String = ReservationStatus.CONFIRMED.name
)
