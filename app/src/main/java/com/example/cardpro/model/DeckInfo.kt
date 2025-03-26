package com.example.cardpro.model

/**
 * デッキの情報を表すデータクラス
 */
data class DeckInfo(
    val name: String,
    val description: String,
    val deckType: String,
    val cards: Map<CardInfo, Int> = emptyMap()
) {
    // カードの種類数
    val cardTypeCount: Int
        get() = cards.size
        
    // カードの総枚数を計算
    val cardCount: Int
        get() = cards.values.sum()
        
    // カードのリストを取得（同じカードが複数枚ある場合は複数回含まれる）
    fun getCardList(): List<CardInfo> {
        val result = mutableListOf<CardInfo>()
        cards.forEach { (card, count) ->
            repeat(count) {
                result.add(card)
            }
        }
        return result
    }
}
