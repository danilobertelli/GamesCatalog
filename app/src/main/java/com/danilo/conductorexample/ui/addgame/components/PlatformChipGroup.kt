package com.danilo.conductorexample.ui.addgame.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danilo.conductorexample.R
import com.danilo.conductorexample.domain.model.Platform
import com.danilo.conductorexample.ui.theme.ConductorExampleTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlatformChipGroup(
    availablePlatforms: List<Platform>,
    selectedPlatforms: Set<String>,
    onPlatformToggled: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.add_game_section_platforms),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (availablePlatforms.isEmpty()) {
            Text(
                text = stringResource(R.string.add_game_platforms_loading),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        } else {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availablePlatforms.forEach { platform ->
                    val isSelected = selectedPlatforms.contains(platform.name)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onPlatformToggled(platform.name) },
                        label = {
                            Text(text = platform.name)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlatformChipGroupPreview() {
    ConductorExampleTheme {
        PlatformChipGroup(
            availablePlatforms = listOf(
                Platform("ps5", "PlayStation 5"),
                Platform("switch", "Nintendo Switch"),
                Platform("pc", "PC (Windows)"),
                Platform("xbox", "Xbox Series X/S")
            ),
            selectedPlatforms = setOf("PlayStation 5", "PC (Windows)"),
            onPlatformToggled = {}
        )
    }
}
