package com.example.cardpro.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.UUID

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
@Entity(tableName = "decks")
data class DeckInfo(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val deckType: String,
    @Ignore
    val cards: Map<CardInfo, List<String>> = emptyMap()
) {
    // セカンダリコンストラクタ（Roomで使用）
    constructor(
        id: String,
        name: String,
        description: String,
        deckType: String
    ) : this(id, name, description, deckType, emptyMap())
    
    // カードの種類数
    @Ignore
    val cardTypeCount: Int
        get() = cards.size
        
    // カードの総枚数を計算
    @Ignore
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

/**
 * デッキとカードの関連付けを表すエンティティ
 */
@Entity(
    tableName = "deck_card_locations",
    primaryKeys = ["deckId", "cardId", "location"]
)
data class DeckCardLocation(
    val deckId: String,
    val cardId: String,
    val location: String
)
