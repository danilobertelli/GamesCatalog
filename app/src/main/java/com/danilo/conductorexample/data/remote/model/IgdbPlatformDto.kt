package com.danilo.conductorexample.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IgdbPlatformDto(
    @SerialName("id") val id: Long = 0,
    @SerialName("name") val name: String = ""
)
