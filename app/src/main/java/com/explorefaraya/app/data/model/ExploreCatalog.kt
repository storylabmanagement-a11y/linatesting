package com.explorefaraya.app.data.model

import android.content.Context

private val CATEGORY_MAP: Map<String, SiteCategory> = mapOf(
    "Chalets & Guesthouses" to SiteCategory.DINING,
    "Hotels" to SiteCategory.DINING,
    "Lebanese Restaurants" to SiteCategory.DINING,
    "International Cuisine" to SiteCategory.DINING,
    "Coffee Shops" to SiteCategory.DINING,
    "Sweets & Desserts" to SiteCategory.DINING,
    "Night Life & Pubs" to SiteCategory.NIGHTLIFE,
    "ATV & Skidoo" to SiteCategory.ADVENTURE,
    "Activity Hub" to SiteCategory.ADVENTURE,
    "Ski Shops" to SiteCategory.SKI,
    "Hiking" to SiteCategory.NATURE,
    "Camping & Campsites" to SiteCategory.NATURE,
    "Churches & Religious Tourism" to SiteCategory.CULTURAL
)

/** Directory categories excluded from the curated Explore experience (off-brand utility listings). */
private val EXCLUDED_CATEGORIES = setOf(
    "Markets", "Snacks", "Bakeries & Saj", "Others", "Butchers", "Shisha Shops",
    "Dollar Stores", "Electronic Stores", "Money Transfer", "Gas Stations", "Taxi",
    "Hairdressers", "Pharmacies", "Kids Animation & Fun", "Decoration",
    "Board Games", "Diet & Nutrition", "Government Representation", "Clothing & Lingerie"
)

private val BOOKABLE_SOURCE_CATEGORIES = setOf(
    "Chalets & Guesthouses", "Hotels", "Lebanese Restaurants", "International Cuisine"
)

private val CULTURAL_HOMEPAGE_SPOTS = setOf("Saint Charbel", "The Cross")

/** Loads the curated "Faraya & Beyond" touristic sites: filtered/remapped from the real
 * explorefaraya.com directory CSV, plus a handful of editorially seeded flagship sites. */
object ExploreCatalog {
    private var _sites: List<TouristSite> = emptyList()
    val sites: List<TouristSite> get() = _sites

    fun init(context: Context) {
        if (_sites.isNotEmpty()) return
        val fromCsv = mutableListOf<TouristSite>()
        context.assets.open("explore_listings.csv").bufferedReader(Charsets.UTF_8).useLines { lines ->
            val iterator = lines.iterator()
            if (iterator.hasNext()) iterator.next() // skip header
            var index = 0
            while (iterator.hasNext()) {
                val line = iterator.next()
                if (line.isBlank()) continue
                val fields = parseCsvLine(line)
                if (fields.size < 2) continue
                val rawCategory = fields.getOrElse(0) { "" }.trim()
                if (rawCategory in EXCLUDED_CATEGORIES) { index++; continue }

                val name = fields.getOrElse(1) { "" }.trim()
                val phone = fields.getOrElse(2) { "" }.trim()
                val imageUrl = fields.getOrElse(4) { "" }.trim()

                val siteCategory: SiteCategory = if (rawCategory == "Popular Touristic Destinations (Homepage)") {
                    if (name in CULTURAL_HOMEPAGE_SPOTS) SiteCategory.CULTURAL else SiteCategory.NATURE
                } else {
                    val mapped = CATEGORY_MAP[rawCategory]
                    if (mapped == null) { index++; continue }
                    mapped
                }

                fromCsv.add(
                    TouristSite(
                        id = "csv-$index",
                        name = name,
                        category = siteCategory,
                        shortDescription = "A local favorite in the Faraya & Beyond region.",
                        longDescription = "Part of the Faraya & Beyond directory. Full editorial description coming soon.",
                        photos = if (imageUrl.isNotBlank()) listOf(imageUrl) else emptyList(),
                        hours = "Contact for hours",
                        entryFee = "",
                        phone = phone,
                        driveTimeBeirut = "~1h 15m",
                        driveTimeJounieh = "~40 min",
                        bookable = rawCategory in BOOKABLE_SOURCE_CATEGORIES
                    )
                )
                index++
            }
        }

        _sites = fromCsv + editorialSeedSites()
    }

    private fun editorialSeedSites(): List<TouristSite> = listOf(
        TouristSite(
            id = "seed-mzaar",
            name = "Mzaar Ski Resort",
            category = SiteCategory.SKI,
            shortDescription = "The largest ski resort in the Middle East, with slopes for every level.",
            longDescription = "Mzaar Kfardebian is the region's flagship ski destination, with a wide range of runs, lift access, and a lively après-ski scene. Best visited December through March for snow sports, and in summer for mountain views and cooler air.",
            photos = emptyList(),
            hours = "8:30 AM - 4:30 PM (winter season)",
            entryFee = "Day pass pricing varies by season",
            phone = "",
            driveTimeBeirut = "~1h 15m",
            driveTimeJounieh = "~45 min",
            featured = true
        ),
        TouristSite(
            id = "seed-faqra-cliffs",
            name = "Faqra Cliffs",
            category = SiteCategory.NATURE,
            shortDescription = "Dramatic limestone cliffs with sweeping valley views.",
            longDescription = "Faqra's cliffs are one of the region's most photographed natural landmarks — a striking rock formation overlooking the valley, popular for short hikes, photography, and sunset views.",
            photos = emptyList(),
            hours = "Daylight hours",
            entryFee = "Free",
            phone = "",
            driveTimeBeirut = "~1h 20m",
            driveTimeJounieh = "~50 min",
            featured = true
        ),
        TouristSite(
            id = "seed-faqra-ruins",
            name = "Faqra Roman Ruins",
            category = SiteCategory.CULTURAL,
            shortDescription = "Ancient Roman temple ruins set against the mountains.",
            longDescription = "A well-preserved Roman archaeological site featuring temple remains dating back centuries, set in a striking mountain landscape — a quieter alternative to Lebanon's larger Roman sites.",
            photos = emptyList(),
            hours = "9:00 AM - 5:00 PM",
            entryFee = "Small entry fee",
            phone = "",
            driveTimeBeirut = "~1h 20m",
            driveTimeJounieh = "~50 min"
        )
    )

    fun findById(id: String): TouristSite? = _sites.firstOrNull { it.id == id }

    fun byCategory(category: SiteCategory): List<TouristSite> = _sites.filter { it.category == category }

    fun featured(): List<TouristSite> = _sites.filter { it.featured }
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
