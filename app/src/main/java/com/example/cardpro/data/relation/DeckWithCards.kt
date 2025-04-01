package com.example.cardpro.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.DeckCardLocation
import com.example.cardpro.model.DeckInfo

/**
 * デッキとそれに含まれるカードの関連を表すクラス
 */
data class DeckWithCards(
    @Embedded val deck: DeckInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = DeckCardLocation::class,
            parentColumn = "deckId",
            entityColumn = "cardId"
        )
    )
    val cards: List<CardInfo>
)

/**
 * カードとそれが含まれるデッキの関連を表すクラス
 */
data class CardWithDecks(
    @Embedded val card: CardInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = DeckCardLocation::class,
            parentColumn = "cardId",
            entityColumn = "deckId"
        )
    )
    val decks: List<DeckInfo>
)
