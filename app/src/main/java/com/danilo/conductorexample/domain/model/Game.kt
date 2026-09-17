package com.danilo.conductorexample.domain.model

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
