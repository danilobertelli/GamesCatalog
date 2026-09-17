package com.danilo.conductorexample.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.danilo.conductorexample.data.local.converter.Converters
import com.danilo.conductorexample.data.local.dao.GameDao
import com.danilo.conductorexample.data.local.dao.PlatformDao
import com.danilo.conductorexample.data.local.entity.GameEntity
import com.danilo.conductorexample.data.local.entity.PlatformEntity

@Database(
    entities = [GameEntity::class, PlatformEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GamesCatalogDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun platformDao(): PlatformDao
}
