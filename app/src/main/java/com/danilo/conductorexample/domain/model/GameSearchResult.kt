package com.danilo.conductorexample.domain.model

/**
 * Domain representation of a game search result returned by remote APIs (such as IGDB).
 *
 * Used for debounced autocomplete suggestions in the add-game flow.
 *
 * @property id Remote identifier of the game on the external service.
 * @property title Title of the game.
 * @property overview Summary, description, or storyline of the game.
 * @property coverImageUrl Optional HTTPS URL to the game's high-resolution cover image.
 * @property platformNames List of platform names where the game was officially released.
 * @property releaseYear Optional initial release year of the game.
 */
data class GameSearchResult(
    val id: String,
    val title: String,
    val overview: String,
    val coverImageUrl: String?,
    val platformNames: List<String>,
    val releaseYear: Int? = null
)
