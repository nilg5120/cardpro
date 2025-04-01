package com.example.cardpro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cardpro.model.CardInfo

/**
 * カードデータを管理するViewModel
 */
class CardViewModel : ViewModel() {
    // カード一覧
    private val _cards = mutableStateListOf<CardInfo>()
    val cards: List<CardInfo> get() = _cards

    // 編集中のカード
    private val _currentCard = mutableStateOf<CardInfo?>(null)
    val currentCard get() = _currentCard.value

    // ダイアログの表示状態
    private val _showAddDialog = mutableStateOf(false)
    val showAddDialog get() = _showAddDialog.value

    private val _showEditDialog = mutableStateOf(false)
    val showEditDialog get() = _showEditDialog.value

    private val _showDeleteDialog = mutableStateOf(false)
    val showDeleteDialog get() = _showDeleteDialog.value

    init {
        // 初期データの設定
        _cards.addAll(
            listOf(
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
        )
    }

    /**
     * カードを追加する
     */
    fun addCard(card: CardInfo) {
        _cards.add(card)
        hideAddDialog()
    }

    /**
     * カードを更新する
     */
    fun updateCard(card: CardInfo) {
        val index = _cards.indexOfFirst { it.name == currentCard?.name }
        if (index != -1) {
            _cards[index] = card
        }
        hideEditDialog()
    }

    /**
     * カードを削除する
     */
    fun deleteCard() {
        currentCard?.let { card ->
            _cards.removeAll { it.name == card.name }
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
    fun showEditDialog(card: CardInfo) {
        _currentCard.value = card
        _showEditDialog.value = true
    }

    /**
     * 編集ダイアログを非表示にする
     */
    fun hideEditDialog() {
        _showEditDialog.value = false
        _currentCard.value = null
    }

    /**
     * 削除ダイアログを表示する
     */
    fun showDeleteDialog(card: CardInfo) {
        _currentCard.value = card
        _showDeleteDialog.value = true
    }

    /**
     * 削除ダイアログを非表示にする
     */
    fun hideDeleteDialog() {
        _showDeleteDialog.value = false
        _currentCard.value = null
    }
}
