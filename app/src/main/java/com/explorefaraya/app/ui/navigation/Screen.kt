package com.explorefaraya.app.ui.navigation

import java.net.URLDecoder
import java.net.URLEncoder

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Onboarding : Screen("onboarding")

    data object Home : Screen("home")
    data object Explore : Screen("explore")
    data object Events : Screen("events")
    data object Bookings : Screen("bookings")
    data object Profile : Screen("profile")

    data object Search : Screen("search")
    data object Subscription : Screen("subscription")

    data object SiteDetail : Screen("site_detail/{siteId}") {
        fun createRoute(siteId: String) = "site_detail/$siteId"
    }

    data object EventDetail : Screen("event_detail/{eventId}") {
        fun createRoute(eventId: String) = "event_detail/$eventId"
    }

    data object ListingPayment : Screen("listing_payment/{listingId}/{partySize}/{scheduledFor}") {
        fun createRoute(listingId: String, partySize: Int, scheduledFor: String): String {
            val encoded = URLEncoder.encode(scheduledFor, "UTF-8")
            return "listing_payment/$listingId/$partySize/$encoded"
        }
        fun decode(value: String): String = URLDecoder.decode(value, "UTF-8")
    }

    data object EventCheckout : Screen("event_checkout/{eventId}/{tierName}/{quantity}") {
        fun createRoute(eventId: String, tierName: String, quantity: Int): String {
            val encoded = URLEncoder.encode(tierName, "UTF-8")
            return "event_checkout/$eventId/$encoded/$quantity"
        }
        fun decode(value: String): String = URLDecoder.decode(value, "UTF-8")
    }

    data object ReservationDetail : Screen("reservation_detail/{reservationId}") {
        fun createRoute(reservationId: String) = "reservation_detail/$reservationId"
    }
}

val bottomNavItems = listOf(Screen.Home, Screen.Explore, Screen.Events, Screen.Bookings, Screen.Profile)
