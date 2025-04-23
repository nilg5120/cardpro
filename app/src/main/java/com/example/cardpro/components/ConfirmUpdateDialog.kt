package com.example.cardpro.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.cardpro.model.CardInfo

@Composable
fun ConfirmUpdateDialog(
    updated: CardInfo?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("確認") },
        text = { Text("情報を更新しますか？") },
        confirmButton = {
            TextButton(onClick = {
                if (updated != null) {
                    onConfirm()
                } else {
                    onDismiss()
                }
            }) {
                Text("はい")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDismiss) {
                    Text("いいえ")
                }
                Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                TextButton(onClick = onCancel) {
                    Text("キャンセル")
                }
            }
        }
    )
}
