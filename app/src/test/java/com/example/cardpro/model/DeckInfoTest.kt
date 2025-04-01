package com.example.cardpro.model

import org.junit.Assert.*
import org.junit.Test

class DeckInfoTest {

    @Test
    fun testCardTypeCount() {
        // テスト用のカード作成
        val card1 = CardInfo(name = "テストカード1", cost = 1, attack = 1, defense = 1, rarity = "コモン")
        val card2 = CardInfo(name = "テストカード2", cost = 2, attack = 2, defense = 2, rarity = "レア")
        
        // カードマップ作成
        val cardsMap = mapOf(
            card1 to listOf("デッキ1", "デッキ2"),
            card2 to listOf("デッキ1")
        )
        
        // テスト対象のデッキ作成
        val deck = DeckInfo(
            name = "テストデッキ",
            description = "テスト用デッキ",
            deckType = "テスト",
            cards = cardsMap
        )
        
        // カードの種類数が2であることを確認
        assertEquals(2, deck.cardTypeCount)
    }
    
    @Test
    fun testCardCount() {
        // テスト用のカード作成
        val card1 = CardInfo(name = "テストカード1", cost = 1, attack = 1, defense = 1, rarity = "コモン")
        val card2 = CardInfo(name = "テストカード2", cost = 2, attack = 2, defense = 2, rarity = "レア")
        
        // カードマップ作成
        val cardsMap = mapOf(
            card1 to listOf("デッキ1", "デッキ2"),  // 2枚
            card2 to listOf("デッキ1")             // 1枚
        )
        
        // テスト対象のデッキ作成
        val deck = DeckInfo(
            name = "テストデッキ",
            description = "テスト用デッキ",
            deckType = "テスト",
            cards = cardsMap
        )
        
        // カードの総枚数が3であることを確認
        assertEquals(3, deck.cardCount)
    }
    
    @Test
    fun testGetCardWithLocations() {
        // テスト用のカード作成
        val card1 = CardInfo(name = "テストカード1", cost = 1, attack = 1, defense = 1, rarity = "コモン")
        val card2 = CardInfo(name = "テストカード2", cost = 2, attack = 2, defense = 2, rarity = "レア")
        
        // カードマップ作成
        val cardsMap = mapOf(
            card1 to listOf("デッキ1", "デッキ2"),
            card2 to listOf("デッキ1")
        )
        
        // テスト対象のデッキ作成
        val deck = DeckInfo(
            name = "テストデッキ",
            description = "テスト用デッキ",
            deckType = "テスト",
            cards = cardsMap
        )
        
        // カードと保管場所のペアのリストを取得
        val cardWithLocations = deck.getCardWithLocations()
        
        // リストのサイズが3であることを確認
        assertEquals(3, cardWithLocations.size)
        
        // 各要素の内容を確認
        val expectedLocations = listOf("デッキ1", "デッキ2", "デッキ1")
        val actualLocations = cardWithLocations.map { it.location }
        assertEquals(expectedLocations, actualLocations)
        
        // カードの内容も確認
        assertEquals(card1, cardWithLocations[0].card)
        assertEquals(card1, cardWithLocations[1].card)
        assertEquals(card2, cardWithLocations[2].card)
    }
    
    @Test
    fun testGetCardCount() {
        // テスト用のカード作成
        val card1 = CardInfo(name = "テストカード1", cost = 1, attack = 1, defense = 1, rarity = "コモン")
        val card2 = CardInfo(name = "テストカード2", cost = 2, attack = 2, defense = 2, rarity = "レア")
        val card3 = CardInfo(name = "テストカード3", cost = 3, attack = 3, defense = 3, rarity = "レジェンダリー")
        
        // カードマップ作成
        val cardsMap = mapOf(
            card1 to listOf("デッキ1", "デッキ2"),  // 2枚
            card2 to listOf("デッキ1")             // 1枚
        )
        
        // テスト対象のデッキ作成
        val deck = DeckInfo(
            name = "テストデッキ",
            description = "テスト用デッキ",
            deckType = "テスト",
            cards = cardsMap
        )
        
        // 各カードの枚数を確認
        assertEquals(2, deck.getCardCount(card1))
        assertEquals(1, deck.getCardCount(card2))
        assertEquals(0, deck.getCardCount(card3))  // 存在しないカード
    }
    
    @Test
    fun testEmptyDeck() {
        // 空のデッキを作成
        val emptyDeck = DeckInfo(
            name = "空のデッキ",
            description = "カードがないデッキ",
            deckType = "テスト"
        )
        
        // 各プロパティを確認
        assertEquals(0, emptyDeck.cardTypeCount)
        assertEquals(0, emptyDeck.cardCount)
        assertTrue(emptyDeck.getCardWithLocations().isEmpty())
    }
}
