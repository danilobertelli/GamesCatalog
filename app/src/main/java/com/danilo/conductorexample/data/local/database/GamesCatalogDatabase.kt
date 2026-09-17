package com.danilo.conductorexample.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.danilo.conductorexample.data.local.converter.Converters
import com.danilo.conductorexample.data.local.dao.GameDao
import com.danilo.conductorexample.data.local.entity.GameEntity

@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GamesCatalogDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}
