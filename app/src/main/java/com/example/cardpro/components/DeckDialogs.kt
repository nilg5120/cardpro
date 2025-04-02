package com.example.cardpro.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.DeckInfo
import com.example.cardpro.viewmodel.CardViewModel
import com.example.cardpro.viewmodel.ViewModelProviderFactory

/**
 * デッキ追加ダイアログ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeckDialog(
    onDismiss: () -> Unit,
    onConfirm: (DeckInfo) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deckType by remember { mutableStateOf("バランス型") }
    var expanded by remember { mutableStateOf(false) }

    val deckTypeOptions = listOf("バランス型", "攻撃型", "防御型", "コンボ型", "コントロール型")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("デッキを追加") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("デッキ名") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("説明") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = deckType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("デッキタイプ") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        deckTypeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    deckType = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(
                            DeckInfo(
                                name = name,
                                description = description,
                                deckType = deckType
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

/**
 * デッキ編集ダイアログ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDeckDialog(
    deck: DeckInfo,
    onDismiss: () -> Unit,
    onConfirm: (DeckInfo) -> Unit
) {
    var name by remember { mutableStateOf(deck.name) }
    var description by remember { mutableStateOf(deck.description) }
    var deckType by remember { mutableStateOf(deck.deckType) }
    var expanded by remember { mutableStateOf(false) }

    val deckTypeOptions = listOf("バランス型", "攻撃型", "防御型", "コンボ型", "コントロール型")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("デッキを編集") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("デッキ名") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("説明") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = deckType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("デッキタイプ") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        deckTypeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    deckType = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(
                            DeckInfo(
                                name = name,
                                description = description,
                                deckType = deckType
                            )
                        )
                    }
                }
            ) {
                Text("更新")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}

/**
 * デッキ削除確認ダイアログ
 */
@Composable
fun DeleteDeckDialog(
    deck: DeckInfo,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("デッキを削除") },
        text = { Text("「${deck.name}」を削除してもよろしいですか？") },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
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

/**
 * デッキにカードを追加するダイアログ
 */
@Composable
fun AddCardToDeckDialog(
    onDismiss: () -> Unit,
    onConfirm: (CardInfo, List<String>) -> Unit,
    cardViewModel: CardViewModel = viewModel(
        factory = ViewModelProviderFactory.getCardViewModelFactory(LocalContext.current)
    )
) {
    var selectedCard by remember { mutableStateOf<CardInfo?>(null) }
    var cardCount by remember { mutableIntStateOf(1) }
    var locations by remember { mutableStateOf(List(1) { "" }) }
    
    // 初期値が空の場合はデフォルト値を設定
    if (locations.all { it.isEmpty() }) {
        locations = List(cardCount) { "デフォルト" }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("カードを追加") },
        text = {
            Column {
                Text("追加するカードを選択してください")
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    val cards = cardViewModel.cards.value ?: emptyList()
                    items(cards) { card ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = { selectedCard = card }
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = card.name,
                                    fontWeight = if (selectedCard == card) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedCard == card) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "コスト: ${card.cost} | 攻撃: ${card.attack} | 防御: ${card.defense}",
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // カード枚数選択
                if (selectedCard != null) {
                    Text("追加する枚数を選択してください")
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { 
                                if (cardCount > 1) {
                                    cardCount--
                                    locations = locations.dropLast(1)
                                }
                            },
                            enabled = cardCount > 1
                        ) {
                            Text("-")
                        }
                        
                        Text(
                            text = "$cardCount",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Button(
                            onClick = { 
                                if (cardCount < 4) {
                                    cardCount++
                                    locations = locations + ""
                                }
                            },
                            enabled = cardCount < 4
                        ) {
                            Text("+")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 保管場所入力欄
                    Text("各カードの保管場所を入力してください")
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Column {
                        locations.forEachIndexed { index, location ->
                            OutlinedTextField(
                                value = location,
                                onValueChange = { 
                                    val newLocations = locations.toMutableList()
                                    newLocations[index] = it
                                    locations = newLocations
                                },
                                label = { Text("カード ${index + 1}の保管場所") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedCard?.let { onConfirm(it, locations) }
                },
                enabled = selectedCard != null
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
