package com.danilo.conductorexample.domain.model

/**
 * Represents a video game entity in the user's catalog.
 *
 * Encapsulates core information about a game including its metadata, user play status,
 * user evaluation (1-5 stars), and completion timestamp.
 *
 * @property id Unique identifier for the game (typically a UUID string).
 * @property title Name or title of the video game. Must not be blank.
 * @property overview Brief description, synopsis, or user notes about the game.
 * @property coverImageUrl Optional HTTPS URL to the game's high-resolution cover artwork.
 * @property platforms List of platforms/consoles associated with this game entry.
 * @property status Current progress or backlog state of the game (see [GameStatus]).
 * @property rating Optional user review rating, restricted to values between 1 and 5.
 * @property completionDateEpochMs Optional epoch timestamp in milliseconds when the game was completed.
 */
data class Game(
    val id: String,
    val title: String,
    val overview: String = "",
    val coverImageUrl: String? = null,
    val platforms: List<String> = emptyList(),
    val status: GameStatus = GameStatus.WANT_TO_PLAY,
    val rating: Int? = null,
    val completionDateEpochMs: Long? = null
) {
    init {
        require(title.isNotBlank()) { "Game title cannot be blank" }
        rating?.let {
            require(it in 1..5) { "Rating must be between 1 and 5" }
        }
    }
}
