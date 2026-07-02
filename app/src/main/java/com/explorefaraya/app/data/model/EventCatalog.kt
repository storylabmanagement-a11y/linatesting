package com.explorefaraya.app.data.model

/**
 * Static seed catalog of Faraya summer events shown in the Events tab.
 * PLACEHOLDER CONTENT — replace title/description/date/link with real events from
 * explorefaraya.com once supplied.
 */
object EventCatalog {
    val events = listOf(
        EventItem(
            id = "summer-music-night",
            title = "Summer Music Night",
            description = "Live outdoor concert with local and regional artists, food trucks and mountain views.",
            date = "Sat, Jul 12 · 7:00 PM",
            location = "Faraya Village Square",
            category = "Music",
            accentColorHex = "#8E4EC6",
            link = "https://explorefaraya.com"
        ),
        EventItem(
            id = "mountain-bike-race",
            title = "Faraya Mountain Bike Race",
            description = "Open cross-country race through the summer trails, all skill levels welcome.",
            date = "Sun, Jul 27 · 8:00 AM",
            location = "Faraya Trailhead",
            category = "Sports",
            accentColorHex = "#2E9E5B",
            link = "https://explorefaraya.com"
        ),
        EventItem(
            id = "food-and-wine-fair",
            title = "Food & Wine Fair",
            description = "Local producers, wineries and chefs showcase Faraya's food scene over a full weekend.",
            date = "Fri-Sun, Aug 8-10",
            location = "Faraya Village Center",
            category = "Food",
            accentColorHex = "#F2994A",
            link = "https://explorefaraya.com"
        ),
        EventItem(
            id = "stargazing-night",
            title = "Guided Stargazing Night",
            description = "Telescopes, a local astronomer guide and hot drinks under Faraya's clear summer sky.",
            date = "Sat, Aug 16 · 9:00 PM",
            location = "Faraya Summit Overlook",
            category = "Nature",
            accentColorHex = "#1B4D6B",
            link = "https://explorefaraya.com"
        ),
        EventItem(
            id = "kids-summer-carnival",
            title = "Kids Summer Carnival",
            description = "Games, face painting and rides for the whole family.",
            date = "Sun, Aug 24 · 11:00 AM",
            location = "Faraya Village Square",
            category = "Family",
            accentColorHex = "#D64545",
            link = "https://explorefaraya.com"
        ),
        EventItem(
            id = "sunset-yoga",
            title = "Sunset Yoga on the Ridge",
            description = "All-levels outdoor yoga session followed by a group sunset watch.",
            date = "Every Wed · 6:30 PM",
            location = "Faraya Ridge Trail",
            category = "Wellness",
            accentColorHex = "#2E86AB",
            link = "https://explorefaraya.com"
        )
    )

    fun findById(id: String): EventItem? = events.firstOrNull { it.id == id }
}
