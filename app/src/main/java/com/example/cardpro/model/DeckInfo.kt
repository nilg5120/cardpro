package com.example.cardpro.model

/**
 * カードとその保管場所の情報を表すデータクラス
 */
data class CardWithLocation(
    val card: CardInfo,
    val location: String
)

/**
 * デッキの情報を表すデータクラス
 */
data class DeckInfo(
    val name: String,
    val description: String,
    val deckType: String,
    val cards: Map<CardInfo, List<String>> = emptyMap()
) {
    // カードの種類数
    val cardTypeCount: Int
        get() = cards.size
        
    // カードの総枚数を計算
    val cardCount: Int
        get() = cards.values.sumOf { it.size }
        
    // カードと保管場所のペアのリストを取得
    fun getCardWithLocations(): List<CardWithLocation> {
        val result = mutableListOf<CardWithLocation>()
        cards.forEach { (card, locations) ->
            locations.forEach { location ->
                result.add(CardWithLocation(card, location))
            }
        }
        return result
    }
    
    // 特定のカードの枚数を取得
    fun getCardCount(card: CardInfo): Int {
        return cards[card]?.size ?: 0
    }
}
