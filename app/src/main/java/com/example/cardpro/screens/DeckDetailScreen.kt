package com.example.cardpro.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardpro.components.AddCardToDeckDialog
import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.DeckInfo
import com.example.cardpro.viewmodel.DeckViewModel
import com.example.cardpro.viewmodel.ViewModelProviderFactory

/**
 * デッキ詳細画面
 *
 * @param deckId デッキID（デッキ名）
 * @param onNavigateBack 前の画面に戻る処理
 * @param viewModel デッキViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailScreen(
    deckId: String,
    onNavigateBack: () -> Unit = {},
    viewModel: DeckViewModel = viewModel(
        factory = ViewModelProviderFactory.getDeckViewModelFactory(LocalContext.current)
    )
) {
    // 選択されたデッキを取得
    val decks by viewModel.decks.observeAsState(emptyList())
    val deck = decks.find { it.id == deckId || it.name == deckId }

    
    // デッキが見つかった場合のみ表示
    LaunchedEffect(deckId) {
        deck?.let { foundDeck -> 
            viewModel.selectDeck(foundDeck)
        } ?: run {
            // デッキが見つからない場合はログに出力
            println("デッキが見つかりません: $deckId")
            println("利用可能なデッキ: ${decks.map { deck -> "${deck.id} (${deck.name})" }}")
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(deck?.name ?: "デッキ詳細") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.clearSelectedDeck()
                            onNavigateBack()
                        }
                    ) {
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
                onClick = { viewModel.showAddCardDialog() }
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
            deck?.let { currentDeck ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // デッキ情報
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = currentDeck.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = currentDeck.description,
                                fontSize = 14.sp
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "タイプ: ${currentDeck.deckType}",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                
                                Text(
                                    text = "カード枚数: ${currentDeck.cardCount}枚",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    // カードリストのヘッダー
                    Text(
                        text = "収録カード",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Divider()
                    
                    // カードリスト
                    if (currentDeck.cards.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "カードがありません\n右下の+ボタンからカードを追加してください",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(currentDeck.cards.entries.toList()) { entry ->
                                val card = entry.key
                                val count = entry.value
                                DeckCardItem(
                                    card = card,
                                    count = count.size,
                                    locations = count,
                                    currentDeck = currentDeck,
                                    onRemove = { locationIndex -> 
                                        val location = count.getOrNull(locationIndex) ?: ""
                                        viewModel.removeCardFromDeck(card, location) 
                                    },
                                    onRemoveAll = { viewModel.removeAllCardFromDeck(card) }
                                )
                            }
                        }
                    }
                }
                
                // カード追加ダイアログ
                if (viewModel.showAddCardDialog) {
                    AddCardToDeckDialog(
                        onDismiss = { viewModel.hideAddCardDialog() },
                        onConfirm = { card, locations -> viewModel.addCardToDeck(card, locations) }
                    )
                }
            } ?: run {
                // デッキが見つからない場合
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("デッキが見つかりません")
                }
            }
        }
    }
}

/**
 * デッキ内のカードアイテム
 */
@Composable
fun DeckCardItem(
    card: CardInfo,
    count: Int,
    locations: List<String>,
    currentDeck: DeckInfo,
    onRemove: (Int) -> Unit,
    onRemoveAll: () -> Unit
) {
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
                modifier = Modifier.padding(end = 16.dp),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // カード枚数表示
                    Card(
                        modifier = Modifier
                            .size(24.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$count",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                
                Text(
                    text = "レアリティ: ${card.rarity}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                // 保管場所の表示
                if (locations.isNotEmpty()) {
                    Column(
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        locations.forEachIndexed { index, location ->
                            if (location.isNotBlank()) {
                                Text(
                                    text = "保管場所 ${index + 1}: $location",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
            
            // 攻撃力と防御力
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${card.attack}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Text(
                    text = "/",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    text = "${card.defense}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )
            }
            
            // 削除ボタン（1枚削除）
            if (locations.isNotEmpty()) {
                IconButton(
                    onClick = { onRemove(0) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "1枚削除",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            // 全削除ボタン（すべて削除）
            if (count > 1) {
                IconButton(
                    onClick = onRemoveAll,
                    modifier = Modifier.size(36.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "すべて削除",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "全て",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
