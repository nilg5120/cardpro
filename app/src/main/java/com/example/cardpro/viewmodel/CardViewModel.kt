package com.example.cardpro.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cardpro.data.repository.CardRepository
import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.GroupedCardInfo
import kotlinx.coroutines.launch

/**
 * カードデータを管理するViewModel
 */
class CardViewModel(private val repository: CardRepository) : ViewModel() {
    // カード一覧
    private val _cardsFlow = repository.getAllCards().asLiveData()
    val cards get() = _cardsFlow

    // カード一覧に表示する用
    private val _uiCardList = repository.getAllCardsGroupedByName().asLiveData()
    val uiCardList  get() = _uiCardList

    // 編集中のカード
    private val _currentCard = mutableStateOf<CardInfo?>(null)
    val currentCard get() = _currentCard.value
    
    // 編集中のカードリスト（同じ名前のカードが複数ある場合）
    private val _currentCards = mutableStateOf<List<CardInfo>>(emptyList())
    val currentCards get() = _currentCards.value

    // ダイアログの表示状態
    private val _showAddDialog = mutableStateOf(false)
    val showAddDialog get() = _showAddDialog.value

    private val _showEditDialog = mutableStateOf(false)
    val showEditDialog get() = _showEditDialog.value

    private val _showDeleteDialog = mutableStateOf(false)
    val showDeleteDialog get() = _showDeleteDialog.value

    // 初期データの設定
    init {
        // データベースが空の場合は初期データを追加
        viewModelScope.launch {
            val initialCards = listOf(
                CardInfo(name = "ドラゴンナイト", cost = 5, attack = 4, defense = 5, rarity = "レア"),
                CardInfo(name = "ゴブリン", cost = 1, attack = 1, defense = 1, rarity = "コモン"),
                CardInfo(name = "エルフの弓使い", cost = 2, attack = 2, defense = 1, rarity = "コモン"),
                CardInfo(name = "炎の魔術師", cost = 3, attack = 2, defense = 3, rarity = "アンコモン"),
                CardInfo(name = "聖なる騎士", cost = 4, attack = 3, defense = 4, rarity = "レア"),
                CardInfo(name = "暗黒の魔王", cost = 8, attack = 8, defense = 8, rarity = "レジェンダリー"),
                CardInfo(name = "癒しの妖精", cost = 2, attack = 1, defense = 2, rarity = "コモン"),
                CardInfo(name = "巨大ゴーレム", cost = 6, attack = 5, defense = 7, rarity = "レア"),
                CardInfo(name = "雷の精霊", cost = 4, attack = 4, defense = 3, rarity = "アンコモン"),
                CardInfo(name = "古代の竜", cost = 10, attack = 10, defense = 10, rarity = "レジェンダリー")
            )
            repository.insertCards(initialCards)
        }
    }

    /**
     * カードを追加する
     */
    fun addCard(card: CardInfo) {
        viewModelScope.launch {
            repository.insertCard(card)
        }
        hideAddDialog()
    }

    /**
     * カードを更新する
     */
    fun updateCard(card: CardInfo) {
        viewModelScope.launch {
            repository.updateCard(card)
        }
        hideEditDialog()
    }

    /**
     * カードを削除する
     */
    fun deleteCard() {
        currentCard?.let { card ->
            viewModelScope.launch {
                repository.deleteCard(card)
            }
        }
        hideDeleteDialog()
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
    fun showEditDialog(card: GroupedCardInfo) {
        viewModelScope.launch {
            // カード名でSQLクエリを実行して、該当するカードをすべて取得
            val cards = repository.getCardsByName(card.name)
            if (cards.isNotEmpty()) {
                _currentCards.value = cards
                _currentCard.value = cards.first() // 最初のカードを選択
                _showEditDialog.value = true
            } else {
                // カードが見つからない場合は、GroupedCardInfoからCardInfoを作成
                val cardInfo = CardInfo(
                    name = card.name,
                    cost = card.cost,
                    attack = card.attack,
                    defense = card.defense,
                    rarity = card.rarity,
                    location = card.location,
                    memo = card.memo
                )
                _currentCard.value = cardInfo
                _currentCards.value = listOf(cardInfo)
                _showEditDialog.value = true
            }
        }
    }

    /**
     * 編集ダイアログを非表示にする
     */
    fun hideEditDialog() {
        _showEditDialog.value = false
        _currentCard.value = null
        _currentCards.value = emptyList()
    }

    /**
     * 削除ダイアログを表示する
     */
    fun showDeleteDialog(card: GroupedCardInfo) {
        viewModelScope.launch {
            // カード名でSQLクエリを実行して、該当するカードをすべて取得
            val cards = repository.getCardsByName(card.name)
            if (cards.isNotEmpty()) {
                _currentCard.value = cards.first() // 最初のカードを選択
                _showDeleteDialog.value = true
            } else {
                // カードが見つからない場合は、GroupedCardInfoからCardInfoを作成
                val cardInfo = CardInfo(
                    name = card.name,
                    cost = card.cost,
                    attack = card.attack,
                    defense = card.defense,
                    rarity = card.rarity,
                    location = card.location,
                    memo = card.memo
                )
                _currentCard.value = cardInfo
                _showDeleteDialog.value = true
            }
        }
    }

    /**
     * 削除ダイアログを非表示にする
     */
    fun hideDeleteDialog() {
        _showDeleteDialog.value = false
        _currentCard.value = null
        _currentCards.value = emptyList()
    }

    /**
     * ViewModelファクトリ
     */
    class CardViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CardViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
