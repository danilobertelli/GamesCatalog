package com.danilo.conductorexample.ui.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danilo.conductorexample.R
import com.danilo.conductorexample.domain.model.GameStatus

/**
 * Internal representation of a filter option item in the chips row.
 *
 * @property status The [GameStatus] represented, or `null` for "Todos" (all statuses).
 * @property labelResId The string resource ID for the chip label.
 */
private data class StatusFilterOption(
    val status: GameStatus?,
    val labelResId: Int
)

private val filterOptions = listOf(
    StatusFilterOption(status = null, labelResId = R.string.catalog_filter_all),
    StatusFilterOption(status = GameStatus.WANT_TO_PLAY, labelResId = R.string.catalog_filter_want_to_play),
    StatusFilterOption(status = GameStatus.PLAYING, labelResId = R.string.catalog_filter_playing),
    StatusFilterOption(status = GameStatus.COMPLETED, labelResId = R.string.catalog_filter_completed),
    StatusFilterOption(status = GameStatus.ABANDONED, labelResId = R.string.catalog_filter_abandoned)
)

/**
 * A horizontally scrollable row of [FilterChip]s allowing the user to filter games by [GameStatus].
 *
 * Provides options: Todos, Quero Jogar, Jogando, Concluído, and Abandonado.
 *
 * @param selectedStatus The currently selected [GameStatus], or `null` if all games are shown.
 * @param onStatusSelected Callback invoked when a status filter chip is clicked.
 * @param modifier Modifier applied to the outer layout.
 */
@Composable
fun StatusFilterChips(
    selectedStatus: GameStatus?,
    onStatusSelected: (GameStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = filterOptions,
            key = { it.status?.name ?: "ALL" }
        ) { option ->
            val isSelected = option.status == selectedStatus
            FilterChip(
                selected = isSelected,
                onClick = { onStatusSelected(option.status) },
                label = { Text(text = stringResource(option.labelResId)) },
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null
            )
        }
    }
}

@Preview(showBackground = true, name = "Todos Selected")
@Composable
private fun StatusFilterChipsAllSelectedPreview() {
    StatusFilterChips(
        selectedStatus = null,
        onStatusSelected = {}
    )
}

@Preview(showBackground = true, name = "Playing Selected")
@Composable
private fun StatusFilterChipsPlayingSelectedPreview() {
    StatusFilterChips(
        selectedStatus = GameStatus.PLAYING,
        onStatusSelected = {}
    )
}
