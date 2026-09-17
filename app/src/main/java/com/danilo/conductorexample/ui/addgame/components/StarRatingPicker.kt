package com.danilo.conductorexample.ui.addgame.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danilo.conductorexample.R
import com.danilo.conductorexample.ui.theme.ConductorExampleTheme

@Composable
fun StarRatingPicker(
    rating: Int?,
    onRatingChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.add_game_section_rating),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (star in 1..5) {
                val isSelected = rating != null && star <= rating
                IconButton(
                    onClick = {
                        if (rating == star) {
                            onRatingChanged(null)
                        } else {
                            onRatingChanged(star)
                        }
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(R.string.add_game_rating_star_description, star),
                        tint = if (isSelected) Color(0xFFFFB800) else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            if (rating != null) {
                TextButton(
                    onClick = { onRatingChanged(null) },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.add_game_rating_clear),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StarRatingPickerPreview() {
    ConductorExampleTheme {
        StarRatingPicker(
            rating = 4,
            onRatingChanged = {}
        )
    }
}
