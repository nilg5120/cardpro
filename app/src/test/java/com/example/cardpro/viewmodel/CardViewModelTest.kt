package com.example.cardpro.viewmodel

import com.example.cardpro.model.CardInfo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CardViewModelTest {
    
    private lateinit var viewModel: CardViewModel
    
    @Before
    fun setup() {
        viewModel = CardViewModel()
    }
    
    @Test
    fun testInitialState() {
        // 初期状態では10枚のカードが存在することを確認
        assertEquals(10, viewModel.cards.size)
        
        // 初期状態では編集中のカードがnullであることを確認
        assertNull(viewModel.currentCard)
        
        // 初期状態ではダイアログが表示されていないことを確認
        assertFalse(viewModel.showAddDialog)
        assertFalse(viewModel.showEditDialog)
        assertFalse(viewModel.showDeleteDialog)
    }
    
    @Test
    fun testAddCard() {
        // 追加前のカード数を取得
        val initialSize = viewModel.cards.size
        
        // 新しいカードを作成
        val newCard = CardInfo(
            name = "テストカード",
            cost = 3,
            attack = 2,
            defense = 4,
            rarity = "レア"
        )
        
        // カードを追加
        viewModel.addCard(newCard)
        
        // カードが追加されたことを確認
        assertEquals(initialSize + 1, viewModel.cards.size)
        
        // 追加されたカードが存在することを確認
        val addedCard = viewModel.cards.find { it.name == "テストカード" }
        assertNotNull(addedCard)
        assertEquals(3, addedCard?.cost)
        assertEquals(2, addedCard?.attack)
        assertEquals(4, addedCard?.defense)
        assertEquals("レア", addedCard?.rarity)
        
        // ダイアログが非表示になっていることを確認
        assertFalse(viewModel.showAddDialog)
    }
    
    @Test
    fun testUpdateCard() {
        // 更新対象のカードを選択
        val targetCard = viewModel.cards[0]
        viewModel.showEditDialog(targetCard)
        
        // 編集中のカードが設定されていることを確認
        assertEquals(targetCard, viewModel.currentCard)
        
        // 更新用のカードを作成
        val updatedCard = CardInfo(
            id = targetCard.id,  // IDは同じ
            name = targetCard.name,  // 名前は同じ
            cost = 10,  // コストを変更
            attack = 8,  // 攻撃力を変更
            defense = 8,  // 防御力を変更
            rarity = "レジェンダリー",  // レアリティを変更
            location = "特別保管庫",  // 保管場所を変更
            memo = "テスト用に更新されたカード"  // メモを変更
        )
        
        // カードを更新
        viewModel.updateCard(updatedCard)
        
        // カードが更新されたことを確認
        val card = viewModel.cards.find { it.name == targetCard.name }
        assertNotNull(card)
        assertEquals(10, card?.cost)
        assertEquals(8, card?.attack)
        assertEquals(8, card?.defense)
        assertEquals("レジェンダリー", card?.rarity)
        assertEquals("特別保管庫", card?.location)
        assertEquals("テスト用に更新されたカード", card?.memo)
        
        // ダイアログが非表示になっていることを確認
        assertFalse(viewModel.showEditDialog)
        
        // 編集中のカードがnullになっていることを確認
        assertNull(viewModel.currentCard)
    }
    
    @Test
    fun testDeleteCard() {
        // 削除前のカード数を取得
        val initialSize = viewModel.cards.size
        
        // 削除対象のカードを選択
        val targetCard = viewModel.cards[0]
        viewModel.showDeleteDialog(targetCard)
        
        // 編集中のカードが設定されていることを確認
        assertEquals(targetCard, viewModel.currentCard)
        
        // カードを削除
        viewModel.deleteCard()
        
        // カードが削除されたことを確認
        assertEquals(initialSize - 1, viewModel.cards.size)
        
        // 削除されたカードが存在しないことを確認
        val deletedCard = viewModel.cards.find { it.name == targetCard.name }
        assertNull(deletedCard)
        
        // ダイアログが非表示になっていることを確認
        assertFalse(viewModel.showDeleteDialog)
        
        // 編集中のカードがnullになっていることを確認
        assertNull(viewModel.currentCard)
    }
    
    @Test
    fun testDialogVisibility() {
        // 追加ダイアログの表示/非表示
        viewModel.showAddDialog()
        assertTrue(viewModel.showAddDialog)
        
        viewModel.hideAddDialog()
        assertFalse(viewModel.showAddDialog)
        
        // 編集ダイアログの表示/非表示
        val targetCard = viewModel.cards[0]
        viewModel.showEditDialog(targetCard)
        assertTrue(viewModel.showEditDialog)
        assertEquals(targetCard, viewModel.currentCard)
        
        viewModel.hideEditDialog()
        assertFalse(viewModel.showEditDialog)
        assertNull(viewModel.currentCard)
        
        // 削除ダイアログの表示/非表示
        viewModel.showDeleteDialog(targetCard)
        assertTrue(viewModel.showDeleteDialog)
        assertEquals(targetCard, viewModel.currentCard)
        
        viewModel.hideDeleteDialog()
        assertFalse(viewModel.showDeleteDialog)
        assertNull(viewModel.currentCard)
    }
    
    @Test
    fun testCardInitialData() {
        // 初期データに特定のカードが含まれていることを確認
        val dragonKnight = viewModel.cards.find { it.name == "ドラゴンナイト" }
        assertNotNull(dragonKnight)
        assertEquals(5, dragonKnight?.cost)
        assertEquals(4, dragonKnight?.attack)
        assertEquals(5, dragonKnight?.defense)
        assertEquals("レア", dragonKnight?.rarity)
        
        val goblin = viewModel.cards.find { it.name == "ゴブリン" }
        assertNotNull(goblin)
        assertEquals(1, goblin?.cost)
        assertEquals(1, goblin?.attack)
        assertEquals(1, goblin?.defense)
        assertEquals("コモン", goblin?.rarity)
        
        val ancientDragon = viewModel.cards.find { it.name == "古代の竜" }
        assertNotNull(ancientDragon)
        assertEquals(10, ancientDragon?.cost)
        assertEquals(10, ancientDragon?.attack)
        assertEquals(10, ancientDragon?.defense)
        assertEquals("レジェンダリー", ancientDragon?.rarity)
    }
}
