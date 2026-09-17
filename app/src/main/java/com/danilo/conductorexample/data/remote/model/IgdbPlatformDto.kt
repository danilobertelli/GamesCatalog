package com.danilo.conductorexample.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing a platform associated with an IGDB game.
 *
 * @property id IGDB platform numeric identifier.
 * @property name Official platform name.
 */
@Serializable
data class IgdbPlatformDto(
    @SerialName("id") val id: Long = 0,
    @SerialName("name") val name: String = ""
)
