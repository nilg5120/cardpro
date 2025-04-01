package com.example.cardpro.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardpro.viewmodel.ViewModelProviderFactory
import com.example.cardpro.components.AddDeckDialog
import com.example.cardpro.components.DeleteDeckDialog
import com.example.cardpro.components.EditDeckDialog
import com.example.cardpro.model.DeckInfo
import com.example.cardpro.ui.theme.CardproTheme
import com.example.cardpro.viewmodel.DeckViewModel

/**
 * デッキ一覧画面
 *
 * @param onNavigateBack 前の画面に戻る処理
 * @param onDeckClick デッキがクリックされた時の処理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(
    onNavigateBack: () -> Unit = {},
    onDeckClick: (DeckInfo) -> Unit = {},
    viewModel: DeckViewModel = viewModel(
        factory = ViewModelProviderFactory.getDeckViewModelFactory(LocalContext.current)
    )
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("デッキ一覧") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "デッキを追加"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val decks = viewModel.decks.value ?: emptyList()
                items(decks) { deck ->
                DeckItem(
                    deck = deck,
                    onClick = { onDeckClick(deck) },
                    onEdit = { viewModel.showEditDialog(deck) },
                    onDelete = { viewModel.showDeleteDialog(deck) }
                )
                }
            }
            
            // 追加ダイアログ
            if (viewModel.showAddDialog) {
                AddDeckDialog(
                    onDismiss = { viewModel.hideAddDialog() },
                    onConfirm = { viewModel.addDeck(it) }
                )
            }
            
            // 編集ダイアログ
            viewModel.currentDeck?.let { deck ->
                if (viewModel.showEditDialog) {
                    EditDeckDialog(
                        deck = deck,
                        onDismiss = { viewModel.hideEditDialog() },
                        onConfirm = { viewModel.updateDeck(it) }
                    )
                }
                
                // 削除確認ダイアログ
                if (viewModel.showDeleteDialog) {
                    DeleteDeckDialog(
                        deck = deck,
                        onDismiss = { viewModel.hideDeleteDialog() },
                        onConfirm = { viewModel.deleteDeck() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckItem(
    deck: DeckInfo,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // デッキ情報
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = deck.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = deck.description,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "カード数: ${deck.cardCount}枚",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Text(
                        text = "タイプ: ${deck.deckType}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // メニューボタン
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "メニュー"
                    )
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("編集") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "編集"
                            )
                        },
                        onClick = {
                            onEdit()
                            showMenu = false
                        }
                    )
                    
                    DropdownMenuItem(
                        text = { Text("削除") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "削除"
                            )
                        },
                        onClick = {
                            onDelete()
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeckListScreenPreview() {
    CardproTheme {
        DeckListScreen()
    }
}
