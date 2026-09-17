package com.danilo.conductorexample.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
