package com.explorefaraya.app.data.model

/** Static seed catalog of Faraya activities/events shown in the Events tab. */
object EventCatalog {
    val events = listOf(
        EventItem(
            id = "ski-day-pass",
            title = "Faraya Ski Day Pass",
            description = "Full-day lift access to the slopes with equipment rental desk on-site. Great for beginners and experts alike.",
            date = "Every day, 8:30 AM - 4:30 PM",
            location = "Mzaar Kfardebian, Faraya",
            price = 45.0,
            category = "Skiing",
            accentColorHex = "#2E86AB"
        ),
        EventItem(
            id = "night-snow-tubing",
            title = "Night Snow Tubing",
            description = "Floodlit tubing runs under the stars, with hot chocolate stand and bonfire area.",
            date = "Fri & Sat, 6:00 PM - 10:00 PM",
            location = "Faraya Snow Park",
            price = 25.0,
            category = "Snow Fun",
            accentColorHex = "#1B4D6B"
        ),
        EventItem(
            id = "sunrise-hike",
            title = "Sunrise Summit Hike",
            description = "Guided sunrise hike to the ridge overlook with a local mountain guide, includes breakfast.",
            date = "Sat, 5:00 AM - 9:00 AM",
            location = "Faraya Trailhead",
            price = 20.0,
            category = "Hiking",
            accentColorHex = "#F2994A"
        ),
        EventItem(
            id = "atv-mountain-tour",
            title = "ATV Mountain Tour",
            description = "2-hour guided ATV ride through mountain trails and pine forest, safety gear included.",
            date = "Daily, 10:00 AM - 5:00 PM (hourly slots)",
            location = "Faraya Adventure Park",
            price = 60.0,
            category = "Adventure",
            accentColorHex = "#2E9E5B"
        ),
        EventItem(
            id = "live-music-lodge",
            title = "Live Music at The Lodge",
            description = "Evening of live acoustic music, local food trucks and mulled wine by the fire pit.",
            date = "Sun, 7:00 PM - 11:00 PM",
            location = "The Lodge, Faraya Village",
            price = 15.0,
            category = "Music",
            accentColorHex = "#8E4EC6"
        ),
        EventItem(
            id = "kids-snow-camp",
            title = "Kids Snow Camp (Half Day)",
            description = "Supervised snow play, sledding and hot cocoa for children 5-12, includes snacks.",
            date = "Daily, 9:00 AM - 1:00 PM",
            location = "Faraya Snow Park - Kids Zone",
            price = 30.0,
            category = "Family",
            accentColorHex = "#D64545"
        )
    )

    fun findById(id: String): EventItem? = events.firstOrNull { it.id == id }
}
