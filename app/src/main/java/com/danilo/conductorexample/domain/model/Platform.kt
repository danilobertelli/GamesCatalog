package com.danilo.conductorexample.domain.model

/**
 * Represents a gaming platform or console system (e.g. PlayStation 5, PC, Nintendo Switch).
 *
 * @property id Unique persistent identifier for the platform (e.g. "ps5", "pc").
 * @property name Display name of the platform (e.g. "PlayStation 5", "PC (Windows)").
 */
data class Platform(
    val id: String,
    val name: String
) {
    init {
        require(id.isNotBlank()) { "Platform ID cannot be blank" }
        require(name.isNotBlank()) { "Platform name cannot be blank" }
    }
}
