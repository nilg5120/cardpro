package com.example.cardpro.viewmodel

import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.DeckInfo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DeckViewModelTest {
    
    private lateinit var viewModel: DeckViewModel
    
    @Before
    fun setup() {
        viewModel = DeckViewModel()
    }
    
    @Test
    fun testInitialState() {
        // 初期状態では5つのデッキが存在することを確認
        assertEquals(5, viewModel.decks.size)
        
        // 初期状態では選択中のデッキがnullであることを確認
        assertNull(viewModel.selectedDeck)
        
        // 初期状態では編集中のデッキがnullであることを確認
        assertNull(viewModel.currentDeck)
        
        // 初期状態ではダイアログが表示されていないことを確認
        assertFalse(viewModel.showAddDialog)
        assertFalse(viewModel.showEditDialog)
        assertFalse(viewModel.showDeleteDialog)
        assertFalse(viewModel.showAddCardDialog)
    }
    
    @Test
    fun testAddDeck() {
        // 追加前のデッキ数を取得
        val initialSize = viewModel.decks.size
        
        // 新しいデッキを作成
        val newDeck = DeckInfo(
            name = "テストデッキ",
            description = "テスト用デッキ",
            deckType = "テスト"
        )
        
        // デッキを追加
        viewModel.addDeck(newDeck)
        
        // デッキが追加されたことを確認
        assertEquals(initialSize + 1, viewModel.decks.size)
        
        // 追加されたデッキが存在することを確認
        val addedDeck = viewModel.decks.find { it.name == "テストデッキ" }
        assertNotNull(addedDeck)
        assertEquals("テスト用デッキ", addedDeck?.description)
        assertEquals("テスト", addedDeck?.deckType)
        
        // ダイアログが非表示になっていることを確認
        assertFalse(viewModel.showAddDialog)
    }
    
    @Test
    fun testUpdateDeck() {
        // 更新対象のデッキを選択
        val targetDeck = viewModel.decks[0]
        viewModel.showEditDialog(targetDeck)
        
        // 編集中のデッキが設定されていることを確認
        assertEquals(targetDeck, viewModel.currentDeck)
        
        // 更新用のデッキを作成
        val updatedDeck = DeckInfo(
            name = targetDeck.name,  // 名前は同じ
            description = "更新された説明",
            deckType = "更新されたタイプ"
        )
        
        // デッキを更新
        viewModel.updateDeck(updatedDeck)
        
        // デッキが更新されたことを確認
        val deck = viewModel.decks.find { it.name == targetDeck.name }
        assertNotNull(deck)
        assertEquals("更新された説明", deck?.description)
        assertEquals("更新されたタイプ", deck?.deckType)
        
        // ダイアログが非表示になっていることを確認
        assertFalse(viewModel.showEditDialog)
        
        // 編集中のデッキがnullになっていることを確認
        assertNull(viewModel.currentDeck)
    }
    
    @Test
    fun testDeleteDeck() {
        // 削除前のデッキ数を取得
        val initialSize = viewModel.decks.size
        
        // 削除対象のデッキを選択
        val targetDeck = viewModel.decks[0]
        viewModel.showDeleteDialog(targetDeck)
        
        // 編集中のデッキが設定されていることを確認
        assertEquals(targetDeck, viewModel.currentDeck)
        
        // デッキを削除
        viewModel.deleteDeck()
        
        // デッキが削除されたことを確認
        assertEquals(initialSize - 1, viewModel.decks.size)
        
        // 削除されたデッキが存在しないことを確認
        val deletedDeck = viewModel.decks.find { it.name == targetDeck.name }
        assertNull(deletedDeck)
        
        // ダイアログが非表示になっていることを確認
        assertFalse(viewModel.showDeleteDialog)
        
        // 編集中のデッキがnullになっていることを確認
        assertNull(viewModel.currentDeck)
    }
    
    @Test
    fun testSelectDeck() {
        // 選択対象のデッキ
        val targetDeck = viewModel.decks[0]
        
        // デッキを選択
        viewModel.selectDeck(targetDeck)
        
        // 選択中のデッキが設定されていることを確認
        assertEquals(targetDeck, viewModel.selectedDeck)
        
        // 選択中のデッキをクリア
        viewModel.clearSelectedDeck()
        
        // 選択中のデッキがnullになっていることを確認
        assertNull(viewModel.selectedDeck)
    }
    
    @Test
    fun testAddCardToDeck() {
        // 選択対象のデッキ
        val targetDeck = viewModel.decks[0]
        
        // デッキを選択
        viewModel.selectDeck(targetDeck)
        
        // 追加前のカード数を取得
        val initialCardCount = targetDeck.cardCount
        
        // 追加するカードを作成
        val card = CardInfo(
            name = "テストカード",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア"
        )
        
        // 保管場所のリスト
        val locations = listOf("メインデッキ", "サブデッキ")
        
        // カードを追加
        viewModel.addCardToDeck(card, locations)
        
        // カードが追加されたことを確認
        assertEquals(initialCardCount + 2, viewModel.selectedDeck?.cardCount)
        
        // 追加されたカードの枚数を確認
        assertEquals(2, viewModel.selectedDeck?.getCardCount(card))
    }
    
    @Test
    fun testRemoveCardFromDeck() {
        // 選択対象のデッキ
        val targetDeck = viewModel.decks[0]
        
        // デッキを選択
        viewModel.selectDeck(targetDeck)
        
        // 追加するカードを作成
        val card = CardInfo(
            name = "テストカード",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア"
        )
        
        // 保管場所のリスト
        val locations = listOf("メインデッキ", "サブデッキ")
        
        // カードを追加
        viewModel.addCardToDeck(card, locations)
        
        // 追加後のカード数を取得
        val cardCountAfterAdd = viewModel.selectedDeck?.cardCount ?: 0
        
        // カードを1枚削除
        viewModel.removeCardFromDeck(card)
        
        // カードが1枚削除されたことを確認
        assertEquals(cardCountAfterAdd - 1, viewModel.selectedDeck?.cardCount)
        
        // 残りのカードの枚数を確認
        assertEquals(1, viewModel.selectedDeck?.getCardCount(card))
        
        // 残りのカードをすべて削除
        viewModel.removeAllCardFromDeck(card)
        
        // すべてのカードが削除されたことを確認
        assertEquals(0, viewModel.selectedDeck?.getCardCount(card))
    }
    
    @Test
    fun testDialogVisibility() {
        // 追加ダイアログの表示/非表示
        viewModel.showAddDialog()
        assertTrue(viewModel.showAddDialog)
        
        viewModel.hideAddDialog()
        assertFalse(viewModel.showAddDialog)
        
        // 編集ダイアログの表示/非表示
        val targetDeck = viewModel.decks[0]
        viewModel.showEditDialog(targetDeck)
        assertTrue(viewModel.showEditDialog)
        assertEquals(targetDeck, viewModel.currentDeck)
        
        viewModel.hideEditDialog()
        assertFalse(viewModel.showEditDialog)
        assertNull(viewModel.currentDeck)
        
        // 削除ダイアログの表示/非表示
        viewModel.showDeleteDialog(targetDeck)
        assertTrue(viewModel.showDeleteDialog)
        assertEquals(targetDeck, viewModel.currentDeck)
        
        viewModel.hideDeleteDialog()
        assertFalse(viewModel.showDeleteDialog)
        assertNull(viewModel.currentDeck)
        
        // カード追加ダイアログの表示/非表示
        viewModel.showAddCardDialog()
        assertTrue(viewModel.showAddCardDialog)
        
        viewModel.hideAddCardDialog()
        assertFalse(viewModel.showAddCardDialog)
    }
}
