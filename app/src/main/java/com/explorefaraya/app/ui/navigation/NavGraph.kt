package com.explorefaraya.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.explorefaraya.app.R
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.explorefaraya.app.ui.auth.LoginScreen
import com.explorefaraya.app.ui.auth.OnboardingScreen
import com.explorefaraya.app.ui.auth.SignUpScreen
import com.explorefaraya.app.ui.events.EventDetailScreen
import com.explorefaraya.app.ui.events.EventsScreen
import com.explorefaraya.app.ui.explore.ExploreScreen
import com.explorefaraya.app.ui.explore.SiteDetailScreen
import com.explorefaraya.app.ui.home.HomeScreen
import com.explorefaraya.app.ui.home.SearchScreen
import com.explorefaraya.app.ui.profile.ProfileScreen
import com.explorefaraya.app.ui.reservation.EventCheckoutScreen
import com.explorefaraya.app.ui.reservation.MyBookingsScreen
import com.explorefaraya.app.ui.reservation.PaymentScreen
import com.explorefaraya.app.ui.reservation.ReservationDetailScreen
import com.explorefaraya.app.ui.saved.SavedScreen
import com.explorefaraya.app.ui.subscription.SubscriptionScreen
import com.google.firebase.auth.FirebaseAuth

private fun tabIcon(route: String) = when (route) {
    Screen.Home.route -> Icons.Default.Home
    Screen.Explore.route -> Icons.Default.Explore
    Screen.Events.route -> Icons.Default.CalendarMonth
    Screen.Saved.route -> Icons.Default.Bookmark
    else -> Icons.Default.AccountCircle
}

@Composable
private fun tabLabel(route: String): String = when (route) {
    Screen.Home.route -> stringResource(R.string.nav_home)
    Screen.Explore.route -> stringResource(R.string.nav_explore)
    Screen.Events.route -> stringResource(R.string.nav_events)
    Screen.Saved.route -> stringResource(R.string.nav_saved)
    else -> stringResource(R.string.nav_profile)
}

@Composable
fun ExploreFarayaNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        Screen.Home.route
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
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
                )
            }
            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(Screen.Onboarding.route) {
                OnboardingScreen(onDone = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onExploreClick = { navController.navigate(Screen.Explore.route) },
                    onEventsClick = { navController.navigate(Screen.Events.route) },
                    onPremiumClick = { navController.navigate(Screen.Subscription.route) },
                    onSearchClick = { navController.navigate(Screen.Search.route) },
                    onSiteClick = { id -> navController.navigate(Screen.SiteDetail.createRoute(id)) },
                    onEventClick = { id -> navController.navigate(Screen.EventDetail.createRoute(id)) }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    onBack = { navController.popBackStack() },
                    onSiteClick = { id -> navController.navigate(Screen.SiteDetail.createRoute(id)) },
                    onEventClick = { id -> navController.navigate(Screen.EventDetail.createRoute(id)) }
                )
            }
            composable(Screen.Subscription.route) {
                SubscriptionScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Explore.route) {
                ExploreScreen(onSiteClick = { id -> navController.navigate(Screen.SiteDetail.createRoute(id)) })
            }
            composable(
                route = Screen.SiteDetail.route,
                arguments = listOf(navArgument("siteId") { })
            ) { backStack ->
                val siteId = backStack.arguments?.getString("siteId").orEmpty()
                SiteDetailScreen(
                    siteId = siteId,
                    onBack = { navController.popBackStack() },
                    onBookNow = { id, partySize, scheduledFor ->
                        navController.navigate(Screen.ListingPayment.createRoute(id, partySize, scheduledFor))
                    }
                )
            }
            composable(
                route = Screen.ListingPayment.route,
                arguments = listOf(
                    navArgument("listingId") { },
                    navArgument("partySize") { type = androidx.navigation.NavType.IntType },
                    navArgument("scheduledFor") { }
                )
            ) { backStack ->
                val siteId = backStack.arguments?.getString("listingId").orEmpty()
                val partySize = backStack.arguments?.getInt("partySize") ?: 1
                val scheduledForRaw = backStack.arguments?.getString("scheduledFor").orEmpty()
                val scheduledFor = Screen.ListingPayment.decode(scheduledForRaw)
                PaymentScreen(
                    siteId = siteId,
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
            composable(Screen.Events.route) {
                EventsScreen(onEventClick = { id -> navController.navigate(Screen.EventDetail.createRoute(id)) })
            }
            composable(
                route = Screen.EventDetail.route,
                arguments = listOf(navArgument("eventId") { })
            ) { backStack ->
                val eventId = backStack.arguments?.getString("eventId").orEmpty()
                EventDetailScreen(
                    eventId = eventId,
                    onBack = { navController.popBackStack() },
                    onBuyTickets = { id, tierName, quantity ->
                        navController.navigate(Screen.EventCheckout.createRoute(id, tierName, quantity))
                    }
                )
            }
            composable(
                route = Screen.EventCheckout.route,
                arguments = listOf(
                    navArgument("eventId") { },
                    navArgument("tierName") { },
                    navArgument("quantity") { type = androidx.navigation.NavType.IntType }
                )
            ) { backStack ->
                val eventId = backStack.arguments?.getString("eventId").orEmpty()
                val tierNameRaw = backStack.arguments?.getString("tierName").orEmpty()
                val tierName = Screen.EventCheckout.decode(tierNameRaw)
                val quantity = backStack.arguments?.getInt("quantity") ?: 1
                EventCheckoutScreen(
                    eventId = eventId,
                    tierName = tierName,
                    quantity = quantity,
                    onBack = { navController.popBackStack() },
                    onPaymentSuccess = { reservationId ->
                        navController.navigate(Screen.ReservationDetail.createRoute(reservationId)) {
                            popUpTo(Screen.Events.route)
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
                MyBookingsScreen(onBookingClick = { id -> navController.navigate(Screen.ReservationDetail.createRoute(id)) })
            }
            composable(Screen.Saved.route) {
                SavedScreen(
                    onSiteClick = { id -> navController.navigate(Screen.SiteDetail.createRoute(id)) },
                    onEventClick = { id -> navController.navigate(Screen.EventDetail.createRoute(id)) }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onSignedOut = {
                        navController.navigate(Screen.Login.route) { popUpTo(0) }
                    },
                    onSubscriptionClick = { navController.navigate(Screen.Subscription.route) },
                    onBookingsClick = { navController.navigate(Screen.Bookings.route) },
                    onSavedClick = { navController.navigate(Screen.Saved.route) }
                )
            }
        }
    }
}
