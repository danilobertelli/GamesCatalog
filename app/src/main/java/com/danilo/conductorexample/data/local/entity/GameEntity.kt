package com.danilo.conductorexample.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val overview: String,
    val coverImageUrl: String?,
    val platforms: List<String>,
    val status: String,
    val rating: Int?,
    val completionDateEpochMs: Long?
)
