package com.explorefaraya.app.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")

    data object Dashboard : Screen("dashboard")
    data object Events : Screen("events")
    data object Tickets : Screen("tickets")
    data object Profile : Screen("profile")

    data object EventDetail : Screen("event_detail/{eventId}") {
        fun createRoute(eventId: String) = "event_detail/$eventId"
    }

    data object Payment : Screen("payment/{eventId}/{quantity}") {
        fun createRoute(eventId: String, quantity: Int) = "payment/$eventId/$quantity"
    }

    data object TicketDetail : Screen("ticket_detail/{bookingId}") {
        fun createRoute(bookingId: String) = "ticket_detail/$bookingId"
    }
}

val bottomNavItems = listOf(Screen.Dashboard, Screen.Events, Screen.Tickets, Screen.Profile)
