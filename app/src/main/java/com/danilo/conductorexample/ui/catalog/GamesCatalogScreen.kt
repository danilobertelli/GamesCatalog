package com.danilo.conductorexample.ui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danilo.conductorexample.R
import com.danilo.conductorexample.domain.model.Game
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.ui.catalog.components.CatalogEmptyState
import com.danilo.conductorexample.ui.catalog.components.CatalogSearchBar
import com.danilo.conductorexample.ui.catalog.components.GameListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun GamesCatalogScreen(
    onAddGameClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GamesCatalogViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GamesCatalogContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onAddGameClick = onAddGameClick,
        modifier = modifier
    )
}

@Composable
fun GamesCatalogContent(
    uiState: GamesCatalogUiState,
    onSearchQueryChange: (String) -> Unit,
    onAddGameClick: () -> Unit,
    modifier: Modifier = Modifier,
    onGameClick: (Game) -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.catalog_screen_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                CatalogSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchQueryChange
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddGameClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.catalog_add_game_description)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.isCatalogEmpty -> {
                    CatalogEmptyState(
                        isSearchEmpty = false,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.isSearchEmpty -> {
                    CatalogEmptyState(
                        isSearchEmpty = true,
                        searchQuery = uiState.searchQuery,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.games,
                            key = { it.id }
                        ) { game ->
                            GameListItem(
                                game = game,
                                onClick = { onGameClick(game) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Populated Catalog")
@Composable
private fun GamesCatalogContentPopulatedPreview() {
    GamesCatalogContent(
        uiState = GamesCatalogUiState(
            isLoading = false,
            games = listOf(
                Game(id = "1", title = "Alan Wake 2", rating = 5, status = GameStatus.COMPLETED),
                Game(id = "2", title = "Metroid Prime Remastered", rating = 4, status = GameStatus.PLAYING),
                Game(id = "3", title = "The Legend of Zelda: Tears of the Kingdom", rating = 5, status = GameStatus.COMPLETED)
            ),
            searchQuery = ""
        ),
        onSearchQueryChange = {},
        onAddGameClick = {}
    )
}

@Preview(showBackground = true, name = "Empty Catalog")
@Composable
private fun GamesCatalogContentEmptyPreview() {
    GamesCatalogContent(
        uiState = GamesCatalogUiState(
            isLoading = false,
            games = emptyList(),
            isCatalogEmpty = true
        ),
        onSearchQueryChange = {},
        onAddGameClick = {}
    )
}

@Preview(showBackground = true, name = "Empty Search")
@Composable
private fun GamesCatalogContentSearchEmptyPreview() {
    GamesCatalogContent(
        uiState = GamesCatalogUiState(
            isLoading = false,
            games = emptyList(),
            searchQuery = "Dark Souls",
            isSearchEmpty = true
        ),
        onSearchQueryChange = {},
        onAddGameClick = {}
    )
}
