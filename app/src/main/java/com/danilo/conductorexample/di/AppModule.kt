package com.danilo.conductorexample.di

import androidx.room.Room
import com.danilo.conductorexample.data.local.dao.GameDao
import com.danilo.conductorexample.data.local.dao.PlatformDao
import com.danilo.conductorexample.data.local.database.GamesCatalogDatabase
import com.danilo.conductorexample.data.repository.GameRepositoryImpl
import com.danilo.conductorexample.data.repository.PlatformRepositoryImpl
import com.danilo.conductorexample.domain.repository.GameRepository
import com.danilo.conductorexample.domain.repository.PlatformRepository
import com.danilo.conductorexample.ui.catalog.GamesCatalogViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val databaseModule = module {
    single<GamesCatalogDatabase> {
        Room.databaseBuilder(
            androidContext(),
            GamesCatalogDatabase::class.java,
            "games_catalog.db"
        )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
    }

    single<GameDao> {
        get<GamesCatalogDatabase>().gameDao()
    }

    single<PlatformDao> {
        get<GamesCatalogDatabase>().platformDao()
    }
}

val repositoryModule = module {
    single<GameRepository> {
        GameRepositoryImpl(gameDao = get())
    }

    single<PlatformRepository> {
        PlatformRepositoryImpl(platformDao = get())
    }
}

val viewModelModule = module {
    viewModelOf(::GamesCatalogViewModel)
}

val appModules = listOf(databaseModule, repositoryModule, viewModelModule)
