package com.danilo.conductorexample.domain.model

data class GameSearchResult(
    val id: String,
    val title: String,
    val overview: String,
    val coverImageUrl: String?,
    val platformNames: List<String>,
    val releaseYear: Int? = null
)
