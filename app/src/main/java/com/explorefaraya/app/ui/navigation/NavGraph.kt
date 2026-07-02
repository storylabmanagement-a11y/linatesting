package com.explorefaraya.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.explorefaraya.app.ui.auth.LoginScreen
import com.explorefaraya.app.ui.auth.SignUpScreen
import com.explorefaraya.app.ui.dashboard.DashboardScreen
import com.explorefaraya.app.ui.events.EventDetailScreen
import com.explorefaraya.app.ui.events.EventsScreen
import com.explorefaraya.app.ui.explore.ExploreListingDetailScreen
import com.explorefaraya.app.ui.explore.ExploreScreen
import com.explorefaraya.app.ui.profile.ProfileScreen
import com.explorefaraya.app.ui.reservation.MyBookingsScreen
import com.explorefaraya.app.ui.reservation.PaymentScreen
import com.explorefaraya.app.ui.reservation.ReservationDetailScreen
import com.google.firebase.auth.FirebaseAuth

private fun tabIcon(route: String) = when (route) {
    Screen.Dashboard.route -> Icons.Default.Home
    Screen.Events.route -> Icons.Default.Event
    Screen.Explore.route -> Icons.Default.Explore
    Screen.Bookings.route -> Icons.Default.ConfirmationNumber
    else -> Icons.Default.AccountCircle
}

private fun tabLabel(route: String) = when (route) {
    Screen.Dashboard.route -> "Dashboard"
    Screen.Events.route -> "Events"
    Screen.Explore.route -> "Explore"
    Screen.Bookings.route -> "Bookings"
    else -> "Profile"
}

@Composable
fun ExploreFarayaNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        Screen.Dashboard.route
    } else {
        Screen.Login.route
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tabIcon(screen.route), contentDescription = tabLabel(screen.route)) },
                            label = { Text(tabLabel(screen.route)) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(bottom = if (showBottomBar) padding.calculateBottomPadding() else 0.dp)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
                )
            }
            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            composable(Screen.Events.route) {
                EventsScreen(onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetail.createRoute(eventId))
                })
            }
            composable(
                route = Screen.EventDetail.route,
                arguments = listOf(navArgument("eventId") { })
            ) { backStack ->
                val eventId = backStack.arguments?.getString("eventId").orEmpty()
                EventDetailScreen(
                    eventId = eventId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Explore.route) {
                ExploreScreen(onListingClick = { listingId ->
                    navController.navigate(Screen.ExploreDetail.createRoute(listingId))
                })
            }
            composable(
                route = Screen.ExploreDetail.route,
                arguments = listOf(navArgument("listingId") { })
            ) { backStack ->
                val listingId = backStack.arguments?.getString("listingId").orEmpty()
                ExploreListingDetailScreen(
                    listingId = listingId,
                    onBack = { navController.popBackStack() },
                    onBookNow = { id, partySize, scheduledFor ->
                        navController.navigate(Screen.ReservationPayment.createRoute(id, partySize, scheduledFor))
                    }
                )
            }
            composable(
                route = Screen.ReservationPayment.route,
                arguments = listOf(
                    navArgument("listingId") { },
                    navArgument("partySize") { type = androidx.navigation.NavType.IntType },
                    navArgument("scheduledFor") { }
                )
            ) { backStack ->
                val listingId = backStack.arguments?.getString("listingId").orEmpty()
                val partySize = backStack.arguments?.getInt("partySize") ?: 1
                val scheduledForRaw = backStack.arguments?.getString("scheduledFor").orEmpty()
                val scheduledFor = Screen.ReservationPayment.decodeScheduledFor(scheduledForRaw)
                PaymentScreen(
                    listingId = listingId,
                    partySize = partySize,
                    scheduledFor = scheduledFor,
                    onBack = { navController.popBackStack() },
                    onPaymentSuccess = { reservationId ->
                        navController.navigate(Screen.ReservationDetail.createRoute(reservationId)) {
                            popUpTo(Screen.Explore.route)
                        }
                    }
                )
            }
            composable(
                route = Screen.ReservationDetail.route,
                arguments = listOf(navArgument("reservationId") { })
            ) { backStack ->
                val reservationId = backStack.arguments?.getString("reservationId").orEmpty()
                ReservationDetailScreen(
                    reservationId = reservationId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Bookings.route) {
                MyBookingsScreen(onBookingClick = { reservationId ->
                    navController.navigate(Screen.ReservationDetail.createRoute(reservationId))
                })
            }
            composable(Screen.Profile.route) {
                ProfileScreen(onSignedOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                })
            }
        }
    }
}
