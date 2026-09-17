package com.danilo.conductorexample.di

import androidx.room.Room
import com.danilo.conductorexample.data.local.dao.GameDao
import com.danilo.conductorexample.data.local.database.GamesCatalogDatabase
import com.danilo.conductorexample.data.repository.GameRepositoryImpl
import com.danilo.conductorexample.domain.repository.GameRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<GamesCatalogDatabase> {
        Room.databaseBuilder(
            androidContext(),
            GamesCatalogDatabase::class.java,
            "games_catalog.db"
        ).build()
    }

    single<GameDao> {
        get<GamesCatalogDatabase>().gameDao()
    }
}

val repositoryModule = module {
    single<GameRepository> {
        GameRepositoryImpl(gameDao = get())
    }
}

val appModules = listOf(databaseModule, repositoryModule)
