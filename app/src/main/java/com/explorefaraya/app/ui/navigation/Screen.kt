package com.explorefaraya.app.ui.navigation

import java.net.URLDecoder
import java.net.URLEncoder

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")

    data object Dashboard : Screen("dashboard")
    data object Events : Screen("events")
    data object Explore : Screen("explore")
    data object Bookings : Screen("bookings")
    data object Profile : Screen("profile")

    data object EventDetail : Screen("event_detail/{eventId}") {
        fun createRoute(eventId: String) = "event_detail/$eventId"
    }

    data object ExploreDetail : Screen("explore_detail/{listingId}") {
        fun createRoute(listingId: String) = "explore_detail/$listingId"
    }

    data object ReservationPayment : Screen("reservation_payment/{listingId}/{partySize}/{scheduledFor}") {
        fun createRoute(listingId: String, partySize: Int, scheduledFor: String): String {
            val encoded = URLEncoder.encode(scheduledFor, "UTF-8")
            return "reservation_payment/$listingId/$partySize/$encoded"
        }
        fun decodeScheduledFor(value: String): String = URLDecoder.decode(value, "UTF-8")
    }

    data object ReservationDetail : Screen("reservation_detail/{reservationId}") {
        fun createRoute(reservationId: String) = "reservation_detail/$reservationId"
    }
}

val bottomNavItems = listOf(Screen.Dashboard, Screen.Events, Screen.Explore, Screen.Bookings, Screen.Profile)
