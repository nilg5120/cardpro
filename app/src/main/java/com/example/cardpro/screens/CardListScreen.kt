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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardpro.components.AddCardDialog
import com.example.cardpro.components.DeleteCardDialog
import com.example.cardpro.components.EditCardDialog
import com.example.cardpro.model.CardInfo
import com.example.cardpro.ui.theme.CardproTheme
import com.example.cardpro.viewmodel.CardViewModel

/**
 * カード一覧画面
 *
 * @param onNavigateBack 前の画面に戻る処理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: CardViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カード一覧") },
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
                    contentDescription = "カードを追加"
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
                items(viewModel.cards) { card ->
                    CardItem(
                        card = card,
                        onEdit = { viewModel.showEditDialog(card) },
                        onDelete = { viewModel.showDeleteDialog(card) }
                    )
                }
            }
            
            // 追加ダイアログ
            if (viewModel.showAddDialog) {
                AddCardDialog(
                    onDismiss = { viewModel.hideAddDialog() },
                    onConfirm = { viewModel.addCard(it) }
                )
            }
            
            // 編集ダイアログ
            viewModel.currentCard?.let { card ->
                if (viewModel.showEditDialog) {
                    EditCardDialog(
                        card = card,
                        onDismiss = { viewModel.hideEditDialog() },
                        onConfirm = { viewModel.updateCard(it) }
                    )
                }
                
                // 削除確認ダイアログ
                if (viewModel.showDeleteDialog) {
                    DeleteCardDialog(
                        card = card,
                        onDismiss = { viewModel.hideDeleteDialog() },
                        onConfirm = { viewModel.deleteCard() }
                    )
                }
            }
        }
    }
}

@Composable
fun CardItem(
    card: CardInfo,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // カードのコスト表示
            Column(
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${card.cost}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // カード情報
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = card.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "レアリティ: ${card.rarity}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                if (card.location.isNotBlank()) {
                    Text(
                        text = "保管場所: ${card.location}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 2.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // 攻撃力と防御力
            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${card.attack}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "/",
                    fontSize = 16.sp
                )
                Text(
                    text = "${card.defense}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    modifier = Modifier.padding(start = 8.dp)
                )
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
fun CardListScreenPreview() {
    CardproTheme {
        CardListScreen()
    }
}
