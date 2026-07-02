package com.explorefaraya.app.data.model

/** A user-added item on their personal dashboard (e.g. wishlist / trip-plan entry). */
data class DashboardItem(
    val id: String = "",
    val title: String = "",
    val note: String = "",
    val createdAt: Long = 0L
)

/** A curated event happening in Faraya. No in-app purchase — users tap through to the real listing. */
data class EventItem(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String,
    val accentColorHex: String,
    val link: String
)

enum class ExploreCategory(val label: String) {
    RESTAURANT("Restaurants"),
    ACTIVITY("Activities"),
    CHALET("Chalets"),
    HOTEL("Hotels"),
    TAXI("Taxi"),
    DRIVER("Private Driver"),
    CAMPING("Camping")
}

/** A bookable listing in the Explore section (restaurant, chalet, hotel room, taxi, driver, camping spot...). */
data class ExploreListing(
    val id: String,
    val category: ExploreCategory,
    val title: String,
    val description: String,
    val location: String,
    val price: Double,
    val priceUnit: String,
    val accentColorHex: String,
    val link: String
)

enum class ReservationStatus { CONFIRMED, CANCELLED }

/** A confirmed reservation, persisted per-user after a (mock) payment succeeds. */
data class Reservation(
    val id: String = "",
    val listingId: String = "",
    val listingTitle: String = "",
    val category: String = "",
    val location: String = "",
    val scheduledFor: String = "",
    val partySize: Int = 1,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val confirmationNumber: String = "",
    val createdAt: Long = 0L,
    val status: String = ReservationStatus.CONFIRMED.name
)
