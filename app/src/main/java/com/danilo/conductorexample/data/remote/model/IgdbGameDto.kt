package com.danilo.conductorexample.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing a game returned by IGDB API `/games`.
 *
 * @property id IGDB numeric game identifier.
 * @property name Game title.
 * @property summary Optional textual overview / synopsis.
 * @property cover Optional cover art metadata (see [IgdbCoverDto]).
 * @property platforms List of platforms where the game was released (see [IgdbPlatformDto]).
 * @property firstReleaseDate Optional initial release date represented as Unix timestamp (seconds).
 * @property totalRating Optional aggregated rating score (0.0 to 100.0).
 */
@Serializable
data class IgdbGameDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("summary") val summary: String? = null,
    @SerialName("cover") val cover: IgdbCoverDto? = null,
    @SerialName("platforms") val platforms: List<IgdbPlatformDto> = emptyList(),
    @SerialName("first_release_date") val firstReleaseDate: Long? = null,
    @SerialName("total_rating") val totalRating: Double? = null
)
