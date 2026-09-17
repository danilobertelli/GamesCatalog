package com.danilo.conductorexample.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing cover art metadata returned by the IGDB API.
 *
 * @property id IGDB cover numeric identifier.
 * @property imageId Cloudinary image identifier used to construct high-resolution URLs.
 * @property url Default HTTP image URL returned by the endpoint.
 */
@Serializable
data class IgdbCoverDto(
    @SerialName("id") val id: Long = 0,
    @SerialName("image_id") val imageId: String? = null,
    @SerialName("url") val url: String? = null
)
