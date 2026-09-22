package com.danilo.conductorexample.ui.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danilo.conductorexample.R

@Composable
fun CatalogEmptyState(
    isSearchEmpty: Boolean,
    searchQuery: String = "",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSearchEmpty) Icons.Default.Search else Icons.Default.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isSearchEmpty) {
                stringResource(R.string.catalog_search_empty_title)
            } else {
                stringResource(R.string.catalog_empty_title)
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearchEmpty) {
                if (searchQuery.isBlank()) {
                    stringResource(R.string.catalog_filter_empty_subtitle)
                } else {
                    stringResource(R.string.catalog_search_empty_subtitle, searchQuery)
                }
            } else {
                stringResource(R.string.catalog_empty_subtitle)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogEmptyStateLibraryPreview() {
    CatalogEmptyState(
        isSearchEmpty = false
    )
}

@Preview(showBackground = true)
@Composable
private fun CatalogEmptyStateSearchPreview() {
    CatalogEmptyState(
        isSearchEmpty = true,
        searchQuery = "Cyberpunk"
    )
}
