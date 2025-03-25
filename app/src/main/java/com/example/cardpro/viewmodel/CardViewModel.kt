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
                CardInfo("ドラゴンナイト", 5, 4, 5, "レア"),
                CardInfo("ゴブリン", 1, 1, 1, "コモン"),
                CardInfo("エルフの弓使い", 2, 2, 1, "コモン"),
                CardInfo("炎の魔術師", 3, 2, 3, "アンコモン"),
                CardInfo("聖なる騎士", 4, 3, 4, "レア"),
                CardInfo("暗黒の魔王", 8, 8, 8, "レジェンダリー"),
                CardInfo("癒しの妖精", 2, 1, 2, "コモン"),
                CardInfo("巨大ゴーレム", 6, 5, 7, "レア"),
                CardInfo("雷の精霊", 4, 4, 3, "アンコモン"),
                CardInfo("古代の竜", 10, 10, 10, "レジェンダリー")
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
