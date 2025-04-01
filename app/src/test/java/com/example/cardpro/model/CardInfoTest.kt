package com.example.cardpro.model

import org.junit.Assert.*
import org.junit.Test

class CardInfoTest {

    @Test
    fun testCardInfoCreation() {
        // 基本的なカード情報の作成
        val card = CardInfo(
            name = "テストカード",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア"
        )
        
        // 各フィールドの値を確認
        assertEquals("テストカード", card.name)
        assertEquals(3, card.cost)
        assertEquals(2, card.attack)
        assertEquals(4, card.defense)
        assertEquals("レア", card.rarity)
        assertEquals("", card.location)  // デフォルト値
        assertEquals("", card.memo)      // デフォルト値
        
        // IDが自動生成されていることを確認
        assertNotNull(card.id)
        assertTrue(card.id.isNotEmpty())
    }
    
    @Test
    fun testCardInfoWithLocation() {
        // 保管場所を指定したカード情報の作成
        val card = CardInfo(
            name = "テストカード",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア",
            location = "メインデッキ"
        )
        
        // 保管場所が正しく設定されていることを確認
        assertEquals("メインデッキ", card.location)
    }
    
    @Test
    fun testCardInfoWithMemo() {
        // メモを指定したカード情報の作成
        val card = CardInfo(
            name = "テストカード",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア",
            memo = "このカードは強力な効果を持っています"
        )
        
        // メモが正しく設定されていることを確認
        assertEquals("このカードは強力な効果を持っています", card.memo)
    }
    
    @Test
    fun testCardInfoEquality() {
        // 同じIDを持つ2つのカードを作成
        val id = "test-id-123"
        val card1 = CardInfo(
            id = id,
            name = "テストカード1",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア"
        )
        
        val card2 = CardInfo(
            id = id,
            name = "テストカード1",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア"
        )
        
        // 同じIDを持つカードは等価であることを確認
        assertEquals(card1, card2)
        assertEquals(card1.hashCode(), card2.hashCode())
    }
    
    @Test
    fun testCardInfoInequality() {
        // 異なるIDを持つ2つのカードを作成
        val card1 = CardInfo(
            id = "id1",
            name = "テストカード",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア"
        )
        
        val card2 = CardInfo(
            id = "id2",
            name = "テストカード",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア"
        )
        
        // 異なるIDを持つカードは等価でないことを確認
        assertNotEquals(card1, card2)
    }
    
    @Test
    fun testCardInfoCopy() {
        // 元のカードを作成
        val originalCard = CardInfo(
            name = "元のカード",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア",
            location = "メインデッキ",
            memo = "元のメモ"
        )
        
        // カードをコピーして一部のプロパティを変更
        val copiedCard = originalCard.copy(
            name = "コピーカード",
            cost = 4,
            memo = "新しいメモ"
        )
        
        // 変更されたプロパティを確認
        assertEquals("コピーカード", copiedCard.name)
        assertEquals(4, copiedCard.cost)
        assertEquals("新しいメモ", copiedCard.memo)
        
        // 変更されていないプロパティを確認
        assertEquals(originalCard.id, copiedCard.id)  // IDは同じまま
        assertEquals(2, copiedCard.attack)
        assertEquals(4, copiedCard.defense)
        assertEquals("レア", copiedCard.rarity)
        assertEquals("メインデッキ", copiedCard.location)
    }
}
