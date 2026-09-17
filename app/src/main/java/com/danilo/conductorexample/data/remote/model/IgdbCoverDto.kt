package com.danilo.conductorexample.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IgdbCoverDto(
    @SerialName("id") val id: Long = 0,
    @SerialName("image_id") val imageId: String? = null,
    @SerialName("url") val url: String? = null
)
