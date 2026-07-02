package com.explorefaraya.app.data.model

/** A user-added item on their personal dashboard (e.g. wishlist / trip-plan entry). */
data class DashboardItem(
    val id: String = "",
    val title: String = "",
    val note: String = "",
    val createdAt: Long = 0L
)

/** A curated activity/event happening in Faraya that can be booked. */
data class EventItem(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val price: Double,
    val category: String,
    val accentColorHex: String
)

enum class TicketStatus { PAID, CANCELLED }

/** A confirmed booking/ticket, persisted per-user after a (mock) payment succeeds. */
data class Booking(
    val id: String = "",
    val eventId: String = "",
    val eventTitle: String = "",
    val eventDate: String = "",
    val eventLocation: String = "",
    val quantity: Int = 1,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val ticketNumber: String = "",
    val purchasedAt: Long = 0L,
    val status: String = TicketStatus.PAID.name
)
