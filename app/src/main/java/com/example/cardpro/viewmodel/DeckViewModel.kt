package com.example.cardpro.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cardpro.data.repository.CardRepository
import com.example.cardpro.data.repository.DeckRepository
import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.DeckInfo
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * デッキデータを管理するViewModel
 */
class DeckViewModel(
    private val deckRepository: DeckRepository,
    private val cardRepository: CardRepository
) : ViewModel() {
    // デッキ一覧
    val decks = deckRepository.getAllDecks().asLiveData()

    // 編集中のデッキ
    private val _currentDeck = mutableStateOf<DeckInfo?>(null)
    val currentDeck get() = _currentDeck.value

    // 選択中のデッキ（詳細表示用）
    private val _selectedDeck = mutableStateOf<DeckInfo?>(null)
    val selectedDeck get() = _selectedDeck.value

    // 選択中のデッキに含まれるカード
    private val _selectedDeckCards = mutableStateOf<Map<CardInfo, List<String>>>(emptyMap())

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
        viewModelScope.launch {
            val initialDecks = listOf(
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
            deckRepository.insertDecks(initialDecks)
        }
    }

    /**
     * デッキを追加する
     */
    fun addDeck(deck: DeckInfo) {
        viewModelScope.launch {
            deckRepository.insertDeck(deck)
            hideAddDialog()
        }
    }

    /**
     * デッキを更新する
     */
    fun updateDeck(deck: DeckInfo) {
        viewModelScope.launch {
            // 現在のデッキIDを保持
            currentDeck?.let { currentDeck ->
                // 更新するデッキのIDを保持
                val updatedDeck = deck.copy(id = currentDeck.id)
                deckRepository.updateDeck(updatedDeck)
                
                // 選択中のデッキが更新対象の場合、選択中のデッキも更新
                if (_selectedDeck.value?.id == currentDeck.id) {
                    _selectedDeck.value = updatedDeck
                    loadSelectedDeckCards()
                }
            }
            hideEditDialog()
        }
    }

    /**
     * デッキを削除する
     */
    fun deleteDeck() {
        currentDeck?.let { deck ->
            viewModelScope.launch {
                deckRepository.deleteDeck(deck)
                if (_selectedDeck.value?.id == deck.id) {
                    _selectedDeck.value = null
                    _selectedDeckCards.value = emptyMap()
                }
                hideDeleteDialog()
            }
        }
    }

    /**
     * デッキを選択する（詳細表示用）
     */
    fun selectDeck(deck: DeckInfo) {
        _selectedDeck.value = deck
        loadSelectedDeckCards()
    }

    /**
     * 選択中のデッキをクリアする
     */
    fun clearSelectedDeck() {
        _selectedDeck.value = null
        _selectedDeckCards.value = emptyMap()
    }

    /**
     * 選択中のデッキに含まれるカードを読み込む
     */
    private fun loadSelectedDeckCards() {
        selectedDeck?.let { deck ->
            viewModelScope.launch {
                // デッキとカードの関連付けを取得
                val deckWithCards = deckRepository.getDeckWithCards(deck.id)
                
                // デッキとカードの保管場所の関連付けを取得
                deckRepository.getDeckCardLocations(deck.id).collect { locations ->
                    // カードIDごとに保管場所をグループ化
                    val cardLocationMap = locations.groupBy { it.cardId }
                    val result = mutableMapOf<CardInfo, List<String>>()
                    
                    // 各カードIDに対応するカード情報を取得
                    cardLocationMap.forEach { (cardId, locationList) ->
                        val card = cardRepository.getCardById(cardId)
                        card?.let {
                            result[it] = locationList.map { it.location }
                        }
                    }
                    
                    // 結果を更新
                    _selectedDeckCards.value = result
                }
            }
        }
    }

    /**
     * デッキにカードを追加する（保管場所付き）
     */
    fun addCardToDeck(card: CardInfo, locations: List<String>) {
        selectedDeck?.let { deck ->
            viewModelScope.launch {
                // カードが存在しない場合は追加
                if (cardRepository.getCardById(card.id) == null) {
                    cardRepository.insertCard(card)
                }
                
                // デッキとカードの関連付けを追加
                locations.forEach { location ->
                    deckRepository.addCardToDeck(deck.id, card.id, location)
                }
                
                // 選択中のデッキのカードを再読み込み
                loadSelectedDeckCards()
                
                // ダイアログを閉じる
                hideAddCardDialog()
            }
        }
    }

    /**
     * デッキからカードを1枚削除する（特定の保管場所のカードを削除）
     */
    fun removeCardFromDeck(card: CardInfo, location: String) {
        selectedDeck?.let { deck ->
            viewModelScope.launch {
                deckRepository.removeCardLocationFromDeck(deck.id, card.id, location)
                // 選択中のデッキのカードを再読み込み
                loadSelectedDeckCards()
            }
        }
    }
    
    /**
     * デッキからカードをすべて削除する
     */
    fun removeAllCardFromDeck(card: CardInfo) {
        selectedDeck?.let { deck ->
            viewModelScope.launch {
                deckRepository.removeCardFromDeck(deck.id, card.id)
                // 選択中のデッキのカードを再読み込み
                loadSelectedDeckCards()
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

    /**
     * ViewModelファクトリ
     */
    class DeckViewModelFactory(
        private val deckRepository: DeckRepository,
        private val cardRepository: CardRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DeckViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DeckViewModel(deckRepository, cardRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
