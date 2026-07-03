package com.explorefaraya.app.data.model

import android.content.Context

/**
 * Categories the app treats as bookable (table / room / activity / ride reservation with a
 * mock payment + QR confirmation). Everything else in the directory is browse-and-contact only
 * (call, WhatsApp, directions, website) since there is no real inventory to reserve.
 */
val BOOKABLE_CATEGORIES = setOf(
    "Chalets & Guesthouses",
    "Hotels",
    "Lebanese Restaurants",
    "International Cuisine",
    "ATV & Skidoo",
    "Activity Hub",
    "Hiking",
    "Camping & Campsites",
    "Taxi"
)

/** Loads the real explorefaraya.com directory from a bundled CSV asset (182 listings + 7 homepage spots). */
object ExploreCatalog {
    private var _listings: List<ExploreListing> = emptyList()
    val listings: List<ExploreListing> get() = _listings

    fun init(context: Context) {
        if (_listings.isNotEmpty()) return
        val items = mutableListOf<ExploreListing>()
        context.assets.open("explore_listings.csv").bufferedReader(Charsets.UTF_8).useLines { lines ->
            val iterator = lines.iterator()
            if (iterator.hasNext()) iterator.next() // skip header row
            var index = 0
            while (iterator.hasNext()) {
                val line = iterator.next()
                if (line.isBlank()) continue
                val fields = parseCsvLine(line)
                if (fields.size < 2) continue
                items.add(
                    ExploreListing(
                        id = index.toString(),
                        category = fields.getOrElse(0) { "" }.trim(),
                        name = fields.getOrElse(1) { "" }.trim(),
                        phone = fields.getOrElse(2) { "" }.trim(),
                        linkType = fields.getOrElse(3) { "" }.trim(),
                        imageUrl = fields.getOrElse(4) { "" }.trim()
                    )
                )
                index++
            }
        }
        _listings = items
    }

    fun categories(): List<String> = _listings.map { it.category }.distinct()

    fun byCategory(category: String): List<ExploreListing> = _listings.filter { it.category == category }

    fun findById(id: String): ExploreListing? = _listings.firstOrNull { it.id == id }

    fun isBookable(category: String): Boolean = category in BOOKABLE_CATEGORIES
}

/** Minimal RFC4180-ish CSV line parser: handles quoted fields and doubled "" escapes. */
private fun parseCsvLine(line: String): List<String> {
    val result = mutableListOf<String>()
    val current = StringBuilder()
    var inQuotes = false
    var i = 0
    while (i < line.length) {
        val c = line[i]
        if (inQuotes) {
            if (c == '"') {
                if (i + 1 < line.length && line[i + 1] == '"') {
                    current.append('"')
                    i++
                } else {
                    inQuotes = false
                }
            } else {
                current.append(c)
            }
        } else {
            when (c) {
                '"' -> inQuotes = true
                ',' -> {
                    result.add(current.toString())
                    current.setLength(0)
                }
                else -> current.append(c)
            }
        }
        i++
    }
    result.add(current.toString())
    return result
}
