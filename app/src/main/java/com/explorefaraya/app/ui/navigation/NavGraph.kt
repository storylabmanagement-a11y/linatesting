package com.explorefaraya.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
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
import com.explorefaraya.app.ui.booking.PaymentScreen
import com.explorefaraya.app.ui.dashboard.DashboardScreen
import com.explorefaraya.app.ui.events.EventDetailScreen
import com.explorefaraya.app.ui.events.EventsScreen
import com.explorefaraya.app.ui.profile.ProfileScreen
import com.explorefaraya.app.ui.tickets.MyTicketsScreen
import com.explorefaraya.app.ui.tickets.TicketDetailScreen
import com.google.firebase.auth.FirebaseAuth

private fun tabIcon(route: String) = when (route) {
    Screen.Dashboard.route -> Icons.Default.Home
    Screen.Events.route -> Icons.Default.Event
    Screen.Tickets.route -> Icons.Default.ConfirmationNumber
    else -> Icons.Default.AccountCircle
}

private fun tabLabel(route: String) = when (route) {
    Screen.Dashboard.route -> "Dashboard"
    Screen.Events.route -> "Events"
    Screen.Tickets.route -> "Tickets"
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
                    onBack = { navController.popBackStack() },
                    onBookNow = { id, quantity ->
                        navController.navigate(Screen.Payment.createRoute(id, quantity))
                    }
                )
            }
            composable(
                route = Screen.Payment.route,
                arguments = listOf(
                    navArgument("eventId") { },
                    navArgument("quantity") { type = androidx.navigation.NavType.IntType }
                )
            ) { backStack ->
                val eventId = backStack.arguments?.getString("eventId").orEmpty()
                val quantity = backStack.arguments?.getInt("quantity") ?: 1
                PaymentScreen(
                    eventId = eventId,
                    quantity = quantity,
                    onBack = { navController.popBackStack() },
                    onPaymentSuccess = { bookingId ->
                        navController.navigate(Screen.TicketDetail.createRoute(bookingId)) {
                            popUpTo(Screen.Events.route)
                        }
                    }
                )
            }
            composable(
                route = Screen.TicketDetail.route,
                arguments = listOf(navArgument("bookingId") { })
            ) { backStack ->
                val bookingId = backStack.arguments?.getString("bookingId").orEmpty()
                TicketDetailScreen(
                    bookingId = bookingId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Tickets.route) {
                MyTicketsScreen(onTicketClick = { bookingId ->
                    navController.navigate(Screen.TicketDetail.createRoute(bookingId))
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
