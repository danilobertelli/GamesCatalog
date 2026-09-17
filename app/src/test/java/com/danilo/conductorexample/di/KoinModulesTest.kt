package com.danilo.conductorexample.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.danilo.conductorexample.data.local.dao.GameDao
import com.danilo.conductorexample.data.local.database.GamesCatalogDatabase
import com.danilo.conductorexample.domain.repository.GameRepository
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KoinModulesTest : KoinTest {

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun verifyKoinConfigurationCanResolveDependencies() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        startKoin {
            androidContext(context)
            modules(appModules)
        }

        val database = get<GamesCatalogDatabase>()
        assertNotNull(database)

        val dao = get<GameDao>()
        assertNotNull(dao)

        val platformDao = get<com.danilo.conductorexample.data.local.dao.PlatformDao>()
        assertNotNull(platformDao)

        val repository = get<GameRepository>()
        assertNotNull(repository)

        val platformRepository = get<com.danilo.conductorexample.domain.repository.PlatformRepository>()
        assertNotNull(platformRepository)

        val viewModel = get<com.danilo.conductorexample.ui.catalog.GamesCatalogViewModel>()
        assertNotNull(viewModel)

        val addGameViewModel = get<com.danilo.conductorexample.ui.addgame.AddGameViewModel>()
        assertNotNull(addGameViewModel)

        val gameDetailViewModel = get<com.danilo.conductorexample.ui.detail.GameDetailViewModel> {
            org.koin.core.parameter.parametersOf(androidx.lifecycle.SavedStateHandle())
        }
        assertNotNull(gameDetailViewModel)
    }
}
