package com.danilo.conductorexample.ui.addgame.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danilo.conductorexample.R
import com.danilo.conductorexample.domain.model.GameStatus
import com.danilo.conductorexample.ui.theme.ConductorExampleTheme

@Composable
fun StatusChipGroup(
    selectedStatus: GameStatus,
    onStatusSelected: (GameStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.add_game_section_status),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GameStatus.entries.forEach { status ->
                val labelRes = when (status) {
                    GameStatus.WANT_TO_PLAY -> R.string.add_game_status_want_to_play
                    GameStatus.PLAYING -> R.string.add_game_status_playing
                    GameStatus.COMPLETED -> R.string.add_game_status_completed
                    GameStatus.ABANDONED -> R.string.add_game_status_abandoned
                }
                val isSelected = status == selectedStatus
                FilterChip(
                    selected = isSelected,
                    onClick = { onStatusSelected(status) },
                    label = {
                        Text(text = stringResource(labelRes))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatusChipGroupPreview() {
    ConductorExampleTheme {
        StatusChipGroup(
            selectedStatus = GameStatus.PLAYING,
            onStatusSelected = {}
        )
    }
}
