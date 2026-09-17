package com.danilo.conductorexample.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.danilo.conductorexample.data.local.converter.Converters
import com.danilo.conductorexample.data.local.dao.GameDao
import com.danilo.conductorexample.data.local.dao.PlatformDao
import com.danilo.conductorexample.data.local.entity.GameEntity
import com.danilo.conductorexample.data.local.entity.PlatformEntity

/**
 * Room Database definition for the Games Catalog application.
 *
 * Houses persistent tables for [GameEntity] and [PlatformEntity], registered with [Converters]
 * for list serialization.
 *
 * Version history:
 * - Version 1: Initial schema with games table.
 * - Version 2: Added platforms table and fallback destructive migration for prototyping.
 */
@Database(
    entities = [GameEntity::class, PlatformEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GamesCatalogDatabase : RoomDatabase() {
    /** Provides access to game persistence queries. */
    abstract fun gameDao(): GameDao

    /** Provides access to platform persistence queries. */
    abstract fun platformDao(): PlatformDao
}
