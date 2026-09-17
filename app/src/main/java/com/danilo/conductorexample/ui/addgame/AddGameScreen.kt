package com.danilo.conductorexample.ui.addgame

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danilo.conductorexample.R
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.domain.model.Platform
import com.danilo.conductorexample.ui.addgame.components.PlatformChipGroup
import com.danilo.conductorexample.ui.addgame.components.StarRatingPicker
import com.danilo.conductorexample.ui.addgame.components.StatusChipGroup
import com.danilo.conductorexample.ui.theme.ConductorExampleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddGameScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddGameViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            Toast.makeText(
                context,
                context.getString(R.string.add_game_saved_success),
                Toast.LENGTH_SHORT
            ).show()
            onNavigateBack()
        }
    }

    AddGameContent(
        uiState = uiState,
        onTitleChanged = viewModel::onTitleChanged,
        onStatusSelected = viewModel::onStatusChanged,
        onRatingChanged = viewModel::onRatingChanged,
        onPlatformToggled = viewModel::onPlatformToggled,
        onOverviewChanged = viewModel::onOverviewChanged,
        onSaveClicked = viewModel::onSaveClicked,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGameContent(
    uiState: AddGameUiState,
    onTitleChanged: (String) -> Unit,
    onStatusSelected: (GameStatus) -> Unit,
    onRatingChanged: (Int?) -> Unit,
    onPlatformToggled: (String) -> Unit,
    onOverviewChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_game_screen_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.add_game_back_description)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onSaveClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(R.string.add_game_save_button),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Title Field
            OutlinedTextField(
                value = uiState.title,
                onValueChange = onTitleChanged,
                label = { Text(text = stringResource(R.string.add_game_field_title_label)) },
                placeholder = { Text(text = stringResource(R.string.add_game_field_title_placeholder)) },
                isError = uiState.titleError != null,
                supportingText = uiState.titleError?.let {
                    {
                        Text(
                            text = stringResource(R.string.add_game_field_title_error),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Status Chip Group
            StatusChipGroup(
                selectedStatus = uiState.selectedStatus,
                onStatusSelected = onStatusSelected
            )

            // Star Rating Picker
            StarRatingPicker(
                rating = uiState.rating,
                onRatingChanged = onRatingChanged
            )

            // Platform Multi-Select Chips
            PlatformChipGroup(
                availablePlatforms = uiState.availablePlatforms,
                selectedPlatforms = uiState.selectedPlatforms,
                onPlatformToggled = onPlatformToggled
            )

            // Overview / Notes Field
            OutlinedTextField(
                value = uiState.overview,
                onValueChange = onOverviewChanged,
                label = { Text(text = stringResource(R.string.add_game_section_overview)) },
                placeholder = { Text(text = stringResource(R.string.add_game_field_overview_placeholder)) },
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddGameContentPreview() {
    ConductorExampleTheme {
        AddGameContent(
            uiState = AddGameUiState(
                title = "Elden Ring",
                selectedStatus = GameStatus.PLAYING,
                rating = 5,
                availablePlatforms = listOf(
                    Platform("ps5", "PlayStation 5"),
                    Platform("pc", "PC (Windows)")
                ),
                selectedPlatforms = setOf("PlayStation 5"),
                overview = "Incredible open world."
            ),
            onTitleChanged = {},
            onStatusSelected = {},
            onRatingChanged = {},
            onPlatformToggled = {},
            onOverviewChanged = {},
            onSaveClicked = {},
            onNavigateBack = {}
        )
    }
}
