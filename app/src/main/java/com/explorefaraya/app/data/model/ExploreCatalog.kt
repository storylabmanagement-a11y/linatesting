package com.explorefaraya.app.data.model

/**
 * Static seed catalog for the Explore section.
 * PLACEHOLDER CONTENT — replace with real names/descriptions/photos/links from
 * explorefaraya.com once supplied.
 */
object ExploreCatalog {
    val listings = listOf(
        ExploreListing(
            id = "restaurant-le-sommet",
            category = ExploreCategory.RESTAURANT,
            title = "Le Sommet Restaurant",
            description = "Mountain-view dining with Lebanese and international dishes, cozy fireplace seating.",
            location = "Faraya Village Center",
            price = 0.0,
            priceUnit = "per table",
            accentColorHex = "#F2994A",
            link = "https://explorefaraya.com"
        ),
        ExploreListing(
            id = "activity-atv-tour",
            category = ExploreCategory.ACTIVITY,
            title = "ATV Mountain Tour",
            description = "2-hour guided ATV ride through mountain trails and pine forest, safety gear included.",
            location = "Faraya Adventure Park",
            price = 60.0,
            priceUnit = "per person",
            accentColorHex = "#2E9E5B",
            link = "https://explorefaraya.com"
        ),
        ExploreListing(
            id = "chalet-pine-view",
            category = ExploreCategory.CHALET,
            title = "Pine View Chalet",
            description = "3-bedroom private chalet with a fireplace, terrace and mountain views.",
            location = "Faraya Heights",
            price = 220.0,
            priceUnit = "per night",
            accentColorHex = "#1B4D6B",
            link = "https://explorefaraya.com"
        ),
        ExploreListing(
            id = "hotel-faraya-inn",
            category = ExploreCategory.HOTEL,
            title = "Faraya Inn Hotel Room",
            description = "Comfortable double room with breakfast included, walking distance to the slopes.",
            location = "Faraya Village",
            price = 90.0,
            priceUnit = "per night",
            accentColorHex = "#2E86AB",
            link = "https://explorefaraya.com"
        ),
        ExploreListing(
            id = "taxi-village-transfer",
            category = ExploreCategory.TAXI,
            title = "Village Taxi Transfer",
            description = "On-demand taxi across Faraya village and nearby areas.",
            location = "Faraya Village",
            price = 12.0,
            priceUnit = "per ride",
            accentColorHex = "#12222E",
            link = "https://explorefaraya.com"
        ),
        ExploreListing(
            id = "driver-full-day",
            category = ExploreCategory.DRIVER,
            title = "Private Driver - Full Day",
            description = "A private driver for the day for mountain trips, airport transfer or sightseeing.",
            location = "Faraya & surrounding areas",
            price = 80.0,
            priceUnit = "per day",
            accentColorHex = "#8E4EC6",
            link = "https://explorefaraya.com"
        ),
        ExploreListing(
            id = "camping-pine-forest",
            category = ExploreCategory.CAMPING,
            title = "Pine Forest Campsite",
            description = "Tent and RV spots with fire pits, shared bathrooms and mountain trail access.",
            location = "Faraya Pine Forest",
            price = 15.0,
            priceUnit = "per night",
            accentColorHex = "#2E9E5B",
            link = "https://explorefaraya.com"
        )
    )

    fun findById(id: String): ExploreListing? = listings.firstOrNull { it.id == id }

    fun byCategory(category: ExploreCategory): List<ExploreListing> =
        listings.filter { it.category == category }
}
