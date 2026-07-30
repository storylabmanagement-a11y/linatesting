package com.explorefaraya.app.data.model

import java.util.Calendar
import java.util.TimeZone

private fun beirutMillis(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Beirut"))
    cal.set(year, month - 1, day, hour, minute, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

/**
 * Seed events for Summer 2026, drawn from the real public listings supplied for the
 * Faraya & Beyond brief. Verify dates/lineups/venues closer to launch — festival
 * schedules shift.
 */
object EventCatalog {
    val events = listOf(
        EventItem(
            id = "rivo-sphere",
            title = "Sphere Presents: RIVO",
            organizer = "Sphere",
            venue = "Faraya",
            date = "Thu, Aug 13, 2026",
            dateTimeMillis = beirutMillis(2026, 8, 13, 21, 0),
            description = "Sphere brings RIVO to Faraya for a night of house and techno under the mountain sky.",
            lineup = listOf("RIVO"),
            photos = emptyList(),
            category = "Nightlife",
            tiers = listOf(
                TicketTier("General Admission", 40.0),
                TicketTier("VIP", 90.0, "Priority entry + reserved area")
            ),
            featured = true
        ),
        EventItem(
            id = "2nd-sun-ahlam",
            title = "2nd Sun x Mahmut Orhan x Fideles x Gordo",
            organizer = "2nd Sun",
            venue = "Ahlam Village, Kfardebian",
            date = "Fri-Sat, Aug 14-15, 2026",
            dateTimeMillis = beirutMillis(2026, 8, 14, 20, 0),
            description = "A two-day festival lineup featuring Mahmut Orhan, Fideles, and Gordo at Ahlam Village.",
            lineup = listOf("Mahmut Orhan", "Fideles", "Gordo"),
            photos = emptyList(),
            category = "Festival",
            tiers = listOf(
                TicketTier("Single Day", 55.0),
                TicketTier("Weekend Pass", 95.0),
                TicketTier("VIP Weekend", 160.0, "Both days + VIP area")
            ),
            featured = true
        ),
        EventItem(
            id = "saint-levant-mzaar",
            title = "Saint Levant Live in Concert",
            organizer = "Live Nation-style promoter",
            venue = "Mzaar Ski Resort, Kfardebian",
            date = "Sat, Aug 15, 2026",
            dateTimeMillis = beirutMillis(2026, 8, 15, 21, 0),
            description = "Saint Levant performs live at Mzaar Ski Resort as part of the summer concert season.",
            lineup = listOf("Saint Levant"),
            photos = emptyList(),
            category = "Concert",
            tiers = listOf(
                TicketTier("Standard", 45.0),
                TicketTier("Gold Circle", 100.0, "Closer stage access")
            ),
            featured = true
        ),
        EventItem(
            id = "gims-bekish",
            title = "GIMS Live in Concert",
            organizer = "Regional promoter",
            venue = "Bekish / Faqra",
            date = "Sat, Aug 15, 2026",
            dateTimeMillis = beirutMillis(2026, 8, 15, 21, 30),
            description = "French-Congolese superstar GIMS brings his live show to Faqra.",
            lineup = listOf("GIMS"),
            photos = emptyList(),
            category = "Concert",
            tiers = listOf(
                TicketTier("Standard", 50.0),
                TicketTier("VIP", 120.0, "Front section + fast-track entry")
            ),
            featured = true,
            insiderEarlyAccessUntilMillis = beirutMillis(2026, 7, 20, 0, 0)
        )
    )

    fun findById(id: String): EventItem? = events.firstOrNull { it.id == id }

    fun featured(): List<EventItem> = events.filter { it.featured }
}
