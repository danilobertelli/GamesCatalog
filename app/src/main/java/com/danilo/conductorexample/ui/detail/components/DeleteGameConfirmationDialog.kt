package com.danilo.conductorexample.ui.detail.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.danilo.conductorexample.R

@Composable
fun DeleteGameConfirmationDialog(
    gameTitle: String,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.game_detail_delete_dialog_title),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.game_detail_delete_dialog_message, gameTitle)
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmDelete,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(R.string.game_detail_delete_dialog_confirm),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.game_detail_delete_dialog_cancel))
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun DeleteGameConfirmationDialogPreview() {
    MaterialTheme {
        DeleteGameConfirmationDialog(
            gameTitle = "Elden Ring",
            onConfirmDelete = {},
            onDismiss = {}
        )
    }
}
