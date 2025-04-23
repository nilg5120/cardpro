package com.example.cardpro.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardpro.components.AddCardDialog
import com.example.cardpro.components.DeleteCardDialog
import com.example.cardpro.components.EditCardDialog
import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.GroupedCardInfo
import com.example.cardpro.ui.theme.CardproTheme
import com.example.cardpro.viewmodel.CardViewModel
import com.example.cardpro.viewmodel.ViewModelProviderFactory
import androidx.compose.runtime.livedata.observeAsState
import com.example.cardpro.components.ConfirmUpdateDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: CardViewModel = viewModel(
        factory = ViewModelProviderFactory.getCardViewModelFactory(LocalContext.current)
    )
) {
    var showConfirmCloseDialog by remember { mutableStateOf(false) }
    var pendingUpdatedCard by remember { mutableStateOf<CardInfo?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カード一覧") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            val cards by viewModel.uiCardList.observeAsState(emptyList())

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cards) { card ->
                    CardItem(
                        card = card,
                        onEdit = { viewModel.showEditDialog(card) }
                    )
                }
            }

            if (viewModel.showAddDialog) {
                AddCardDialog(
                    onDismiss = { viewModel.hideAddDialog() },
                    onConfirm = { viewModel.addCard(it) }
                )
            }

            viewModel.currentCards.let { cards ->
                if (viewModel.showEditDialog) {
                    viewModel.currentCard?.let { selectedCard ->
                        EditCardDialog(
                            card = selectedCard,
                            cards = viewModel.currentCards,
                            onDismiss = {
                                showConfirmCloseDialog = true
                            },
                            onConfirm = {
                                pendingUpdatedCard = it
                                showConfirmCloseDialog = true
                            },
                            onDelete = { cardToDelete ->
                                viewModel.setCurrentCard(cardToDelete)
                                viewModel.showDeleteDialog(cardToDelete)
                            }
                        )
                    }
                }

                if (viewModel.showDeleteDialog) {
                    viewModel.currentCard?.let { selectedCard ->
                        DeleteCardDialog(
                            card = selectedCard,
                            onDismiss = { viewModel.hideDeleteDialog() }
                        )
                    }
                }
            }

            if (showConfirmCloseDialog && viewModel.currentCard != null) {
                ConfirmUpdateDialog(
                    updated = pendingUpdatedCard,
                    onConfirm = {
                        // ✅ 更新処理をここで明示的に呼ぶ
                        pendingUpdatedCard?.let {
                            viewModel.updateCard(it)
                        }
                        showConfirmCloseDialog = false
                        viewModel.hideEditDialog()
                    },
                    onDismiss = {
                        showConfirmCloseDialog = false
                        viewModel.hideEditDialog()
                    },
                    onCancel = {
                        showConfirmCloseDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun CardItem(
    card: GroupedCardInfo,
    onEdit: () -> Unit
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
                }
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

                if (card.count > 1) {
                    Text(
                        text = "枚数: ${card.count}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 2.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (card.memo.isNotBlank()) {
                    Text(
                        text = "メモ: ${card.memo}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 2.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

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
