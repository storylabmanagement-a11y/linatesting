package com.explorefaraya.app.data.model

/** Editorial news/feed seed content for the Home tab. Placeholder copy — swap for real posts. */
object NewsCatalog {
    val posts = listOf(
        NewsPost(
            id = "news-1",
            title = "Where to Eat in Faraya This Weekend",
            blurb = "Our picks across Lebanese, international, and late-night bites in the village.",
            imageUrl = "",
            category = "Editorial"
        ),
        NewsPost(
            id = "news-2",
            title = "Snow Report: Mzaar Conditions Update",
            blurb = "Fresh coverage and lift status heading into the weekend.",
            imageUrl = "",
            category = "Snow Report"
        ),
        NewsPost(
            id = "news-3",
            title = "New Venue Opening: A First Look",
            blurb = "A new spot just opened its doors in Kfardebian — here's what to expect.",
            imageUrl = "",
            category = "Openings"
        ),
        NewsPost(
            id = "news-4",
            title = "Road Conditions to Faraya",
            blurb = "Current road status from Beirut and Jounieh routes.",
            imageUrl = "",
            category = "Road Conditions"
        )
    )
}
