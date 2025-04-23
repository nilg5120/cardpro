package com.example.cardpro.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardpro.model.CardInfo
import com.example.cardpro.viewmodel.CardViewModel
import com.example.cardpro.viewmodel.ViewModelProviderFactory

/**
 * カード追加ダイアログ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onConfirm: (CardInfo) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("0") }
    var attack by remember { mutableStateOf("0") }
    var defense by remember { mutableStateOf("0") }
    var rarity by remember { mutableStateOf("コモン") }
    var location by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val rarityOptions = listOf("コモン", "アンコモン", "レア", "レジェンダリー")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("カードを追加") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("カード名") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = cost,
                        onValueChange = { cost = it },
                        label = { Text("コスト") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = attack,
                        onValueChange = { attack = it },
                        label = { Text("攻撃力") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = defense,
                        onValueChange = { defense = it },
                        label = { Text("防御力") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = rarity,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("レアリティ") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        rarityOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    rarity = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 保管場所の入力欄を追加
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("保管場所") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // メモの入力欄を追加
                OutlinedTextField(
                    value = memo,
                    onValueChange = { memo = it },
                    label = { Text("メモ") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val costInt = cost.toIntOrNull() ?: 0
                    val attackInt = attack.toIntOrNull() ?: 0
                    val defenseInt = defense.toIntOrNull() ?: 0
                    
                    if (name.isNotBlank()) {
                        onConfirm(
                            CardInfo(
                                name = name,
                                cost = costInt,
                                attack = attackInt,
                                defense = defenseInt,
                                rarity = rarity,
                                location = location,
                                memo = memo
                            )
                        )
                    }
                }
            ) {
                Text("追加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardDialog(
    card: CardInfo,
    cards: List<CardInfo> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (CardInfo) -> Unit,
    onDelete: (CardInfo) -> Unit = {}
) {
    var selectedCardIndex by remember { mutableIntStateOf(0) }

    var name by remember { mutableStateOf(card.name) }
    var cost by remember { mutableStateOf(card.cost.toString()) }
    var attack by remember { mutableStateOf(card.attack.toString()) }
    var defense by remember { mutableStateOf(card.defense.toString()) }
    var rarity by remember { mutableStateOf(card.rarity) }
    var location by remember { mutableStateOf(card.location) }
    var memo by remember { mutableStateOf(card.memo) }
    var selectedCard by remember { mutableStateOf(card) }
    var expanded by remember { mutableStateOf(false) }
    var cardExpanded by remember { mutableStateOf(false) }

    val rarityOptions = listOf("コモン", "アンコモン", "レア", "レジェンダリー")

    fun buildUpdatedCard(): CardInfo {
        val costInt = cost.toIntOrNull() ?: 0
        val attackInt = attack.toIntOrNull() ?: 0
        val defenseInt = defense.toIntOrNull() ?: 0

        return CardInfo(
            id = selectedCard.id, // ← ここが重要！
            name = name,
            cost = costInt,
            attack = attackInt,
            defense = defenseInt,
            rarity = rarity,
            location = location,
            memo = memo
        )
    }

    AlertDialog(
        onDismissRequest = {
            if (name.isNotBlank()) {
                onConfirm(buildUpdatedCard()) // 外側タップでも更新候補を投げる
            } else {
                onDismiss() // 無効な場合は通常キャンセル
            }
        },
        title = { Text("カードを編集") },
        text = {
            Column {
                if (cards.size > 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = cardExpanded,
                        onExpandedChange = { cardExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = "カード ${selectedCardIndex + 1} (ID: ${card.id.take(8)}...)",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("編集するカード") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cardExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = cardExpanded,
                            onDismissRequest = { cardExpanded = false }
                        ) {
                            cards.forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = { Text("カード ${index + 1} (ID: ${item.id.take(8)}...)") },
                                    onClick = {
                                        selectedCardIndex = index
                                        selectedCard = item // ← これで id を含むすべての情報を更新！
                                        name = item.name
                                        cost = item.cost.toString()
                                        attack = item.attack.toString()
                                        defense = item.defense.toString()
                                        rarity = item.rarity
                                        location = item.location
                                        memo = item.memo
                                        cardExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("カード名") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    OutlinedTextField(value = cost, onValueChange = { cost = it }, label = { Text("コスト") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(value = attack, onValueChange = { attack = it }, label = { Text("攻撃力") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(value = defense, onValueChange = { defense = it }, label = { Text("防御力") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = rarity,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("レアリティ") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        rarityOptions.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = {
                                rarity = option
                                expanded = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("保管場所") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = memo, onValueChange = { memo = it }, label = { Text("メモ") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank()) {
                    onConfirm(buildUpdatedCard())
                }
            }) {
                Text("更新")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDismiss) {
                    Text("キャンセル")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { onDelete(card) }) {
                    Text("削除")
                }
            }
        }
    )
}


/**
 * カード削除確認ダイアログ
 */
@Composable
fun DeleteCardDialog(
    card: CardInfo,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: CardViewModel = viewModel(
        factory = ViewModelProviderFactory.getCardViewModelFactory(context)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("カードを削除") },
        text = { Text("「${card.name}」を削除してもよろしいですか？") },
        confirmButton = {
            Button(onClick = {
                viewModel.deleteCard(card)
                onDismiss() // ダイアログを閉じる
            }) {
                Text("削除")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}