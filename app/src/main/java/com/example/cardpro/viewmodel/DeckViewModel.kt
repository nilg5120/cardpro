package com.example.cardpro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.DeckInfo

/**
 * デッキデータを管理するViewModel
 */
class DeckViewModel : ViewModel() {
    // デッキ一覧
    private val _decks = mutableStateListOf<DeckInfo>()
    val decks: List<DeckInfo> get() = _decks

    // 編集中のデッキ
    private val _currentDeck = mutableStateOf<DeckInfo?>(null)
    val currentDeck get() = _currentDeck.value

    // 選択中のデッキ（詳細表示用）
    private val _selectedDeck = mutableStateOf<DeckInfo?>(null)
    val selectedDeck get() = _selectedDeck.value

    // ダイアログの表示状態
    private val _showAddDialog = mutableStateOf(false)
    val showAddDialog get() = _showAddDialog.value

    private val _showEditDialog = mutableStateOf(false)
    val showEditDialog get() = _showEditDialog.value

    private val _showDeleteDialog = mutableStateOf(false)
    val showDeleteDialog get() = _showDeleteDialog.value

    // カード追加ダイアログの表示状態
    private val _showAddCardDialog = mutableStateOf(false)
    val showAddCardDialog get() = _showAddCardDialog.value

    init {
        // 初期データの設定
        _decks.addAll(
            listOf(
                DeckInfo(
                    name = "スタンダードデッキ",
                    description = "基本的なカードで構成された初心者向けデッキ",
                    deckType = "バランス型"
                ),
                DeckInfo(
                    name = "アグロデッキ",
                    description = "素早く攻撃するための低コストカードを中心としたデッキ",
                    deckType = "攻撃型"
                ),
                DeckInfo(
                    name = "コントロールデッキ",
                    description = "相手の動きを制限しながら有利に進めるデッキ",
                    deckType = "防御型"
                ),
                DeckInfo(
                    name = "ミッドレンジデッキ",
                    description = "中盤での優位性を確保するためのデッキ",
                    deckType = "バランス型"
                ),
                DeckInfo(
                    name = "コンボデッキ",
                    description = "特定のカードの組み合わせで強力な効果を発揮するデッキ",
                    deckType = "コンボ型"
                )
            )
        )
    }

    /**
     * デッキを追加する
     */
    fun addDeck(deck: DeckInfo) {
        _decks.add(deck)
        hideAddDialog()
    }

    /**
     * デッキを更新する
     */
    fun updateDeck(deck: DeckInfo) {
        val index = _decks.indexOfFirst { it.name == currentDeck?.name }
        if (index != -1) {
            // 現在のカードリストを保持
            val currentCards = _decks[index].cards
            // 更新時に現在のカードリストを引き継ぐ
            _decks[index] = deck.copy(cards = currentCards)
        }
        hideEditDialog()
    }

    /**
     * デッキを削除する
     */
    fun deleteDeck() {
        currentDeck?.let { deck ->
            _decks.removeAll { it.name == deck.name }
        }
        hideDeleteDialog()
    }

    /**
     * デッキを選択する（詳細表示用）
     */
    fun selectDeck(deck: DeckInfo) {
        _selectedDeck.value = deck
    }

    /**
     * 選択中のデッキをクリアする
     */
    fun clearSelectedDeck() {
        _selectedDeck.value = null
    }

    /**
     * デッキにカードを追加する（保管場所付き）
     */
    fun addCardToDeck(card: CardInfo, locations: List<String>) {
        selectedDeck?.let { deck ->
            val index = _decks.indexOfFirst { it.name == deck.name }
            if (index != -1) {
                val updatedCards = deck.cards.toMutableMap()
                
                // カードが既に存在するか確認
                val existingCard = updatedCards.keys.find { it.id == card.id }
                
                if (existingCard != null) {
                    // 既存のカードの場合は保管場所を追加
                    val currentLocations = updatedCards[existingCard]?.toMutableList() ?: mutableListOf()
                    currentLocations.addAll(locations)
                    updatedCards[existingCard] = currentLocations
                } else {
                    // 新しいカードの場合は新規追加
                    updatedCards[card] = locations
                }
                
                _decks[index] = deck.copy(cards = updatedCards)
                // 選択中のデッキも更新
                _selectedDeck.value = _decks[index]
            }
        }
        hideAddCardDialog()
    }

    /**
     * デッキからカードを1枚削除する（特定の保管場所のカードを削除）
     */
    fun removeCardFromDeck(card: CardInfo, locationIndex: Int = 0) {
        selectedDeck?.let { deck ->
            val index = _decks.indexOfFirst { it.name == deck.name }
            if (index != -1) {
                val updatedCards = deck.cards.toMutableMap()
                val locations = updatedCards[card]?.toMutableList()
                
                if (locations != null && locations.isNotEmpty() && locationIndex < locations.size) {
                    // 指定されたインデックスの保管場所を削除
                    locations.removeAt(locationIndex)
                    
                    if (locations.isEmpty()) {
                        // すべての保管場所が削除された場合はカード自体を削除
                        updatedCards.remove(card)
                    } else {
                        // 更新された保管場所リストを設定
                        updatedCards[card] = locations
                    }
                    
                    _decks[index] = deck.copy(cards = updatedCards)
                    // 選択中のデッキも更新
                    _selectedDeck.value = _decks[index]
                }
            }
        }
    }
    
    /**
     * デッキからカードをすべて削除する
     */
    fun removeAllCardFromDeck(card: CardInfo) {
        selectedDeck?.let { deck ->
            val index = _decks.indexOfFirst { it.name == deck.name }
            if (index != -1) {
                val updatedCards = deck.cards.toMutableMap()
                updatedCards.remove(card)
                
                _decks[index] = deck.copy(cards = updatedCards)
                // 選択中のデッキも更新
                _selectedDeck.value = _decks[index]
            }
        }
    }

    /**
     * 追加ダイアログを表示する
     */
    fun showAddDialog() {
        _showAddDialog.value = true
    }

    /**
     * 追加ダイアログを非表示にする
     */
    fun hideAddDialog() {
        _showAddDialog.value = false
    }

    /**
     * 編集ダイアログを表示する
     */
    fun showEditDialog(deck: DeckInfo) {
        _currentDeck.value = deck
        _showEditDialog.value = true
    }

    /**
     * 編集ダイアログを非表示にする
     */
    fun hideEditDialog() {
        _showEditDialog.value = false
        _currentDeck.value = null
    }

    /**
     * 削除ダイアログを表示する
     */
    fun showDeleteDialog(deck: DeckInfo) {
        _currentDeck.value = deck
        _showDeleteDialog.value = true
    }

    /**
     * 削除ダイアログを非表示にする
     */
    fun hideDeleteDialog() {
        _showDeleteDialog.value = false
        _currentDeck.value = null
    }

    /**
     * カード追加ダイアログを表示する
     */
    fun showAddCardDialog() {
        _showAddCardDialog.value = true
    }

    /**
     * カード追加ダイアログを非表示にする
     */
    fun hideAddCardDialog() {
        _showAddCardDialog.value = false
    }
}
