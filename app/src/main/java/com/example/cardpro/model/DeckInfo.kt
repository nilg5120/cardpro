package com.example.cardpro.model

/**
 * デッキの情報を表すデータクラス
 */
data class DeckInfo(
    val name: String,
    val description: String,
    val deckType: String,
    val cards: List<CardInfo> = emptyList()
) {
    // カードの枚数を計算
    val cardCount: Int
        get() = cards.size
}
