package com.example.cardpro.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.cardpro.viewmodel.CardViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CardListScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    private lateinit var viewModel: CardViewModel
    
    @Before
    fun setup() {
        viewModel = CardViewModel()
        
        // CardListScreenをセットアップ
        composeTestRule.setContent {
            CardListScreen(
                viewModel = viewModel,
                onNavigateToMenu = {}
            )
        }
    }
    
    @Test
    fun testCardListDisplayed() {
        // 初期状態で10枚のカードが表示されていることを確認
        composeTestRule.onNodeWithText("ドラゴンナイト").assertIsDisplayed()
        composeTestRule.onNodeWithText("ゴブリン").assertIsDisplayed()
        composeTestRule.onNodeWithText("エルフの弓使い").assertIsDisplayed()
        composeTestRule.onNodeWithText("炎の魔術師").assertIsDisplayed()
        composeTestRule.onNodeWithText("聖なる騎士").assertIsDisplayed()
    }
    
    @Test
    fun testCardDetailsDisplayed() {
        // カードの詳細情報が表示されていることを確認
        composeTestRule.onNodeWithText("コスト: 5").assertIsDisplayed()
        composeTestRule.onNodeWithText("攻撃力: 4").assertIsDisplayed()
        composeTestRule.onNodeWithText("防御力: 5").assertIsDisplayed()
        composeTestRule.onNodeWithText("レア").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("コスト: 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("攻撃力: 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("防御力: 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("コモン").assertIsDisplayed()
    }
    
    @Test
    fun testAddButtonDisplayed() {
        // 追加ボタンが表示されていることを確認
        composeTestRule.onNodeWithContentDescription("カードを追加").assertIsDisplayed()
    }
    
    @Test
    fun testAddButtonClickShowsDialog() {
        // 追加ボタンをクリック
        composeTestRule.onNodeWithContentDescription("カードを追加").performClick()
        
        // ダイアログが表示されることを確認
        composeTestRule.onNodeWithText("カードを追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
        composeTestRule.onNodeWithText("追加").assertIsDisplayed()
    }
    
    @Test
    fun testEditButtonClickShowsDialog() {
        // 編集ボタンをクリック
        composeTestRule.onAllNodesWithContentDescription("カードを編集")[0].performClick()
        
        // ダイアログが表示されることを確認
        composeTestRule.onNodeWithText("カードを編集").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
        composeTestRule.onNodeWithText("更新").assertIsDisplayed()
    }
    
    @Test
    fun testDeleteButtonClickShowsDialog() {
        // 削除ボタンをクリック
        composeTestRule.onAllNodesWithContentDescription("カードを削除")[0].performClick()
        
        // ダイアログが表示されることを確認
        composeTestRule.onNodeWithText("カードを削除").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
        composeTestRule.onNodeWithText("削除").assertIsDisplayed()
    }
    
    @Test
    fun testAddNewCard() {
        // 追加ボタンをクリック
        composeTestRule.onNodeWithContentDescription("カードを追加").performClick()
        
        // ダイアログにカード情報を入力
        composeTestRule.onAllNodesWithTag("TextField")[0].performTextInput("テストカード")
        composeTestRule.onAllNodesWithTag("TextField")[1].performTextInput("3")
        composeTestRule.onAllNodesWithTag("TextField")[2].performTextInput("2")
        composeTestRule.onAllNodesWithTag("TextField")[3].performTextInput("4")
        composeTestRule.onAllNodesWithTag("TextField")[4].performTextInput("レア")
        
        // 追加ボタンをクリック
        composeTestRule.onNodeWithText("追加").performClick()
        
        // 新しいカードが追加されたことを確認
        composeTestRule.onNodeWithText("テストカード").assertIsDisplayed()
        composeTestRule.onNodeWithText("コスト: 3").assertIsDisplayed()
        composeTestRule.onNodeWithText("攻撃力: 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("防御力: 4").assertIsDisplayed()
    }
    
    @Test
    fun testSearchFunctionality() {
        // 検索ボックスに入力
        composeTestRule.onNodeWithTag("SearchField").performTextInput("ドラゴン")
        
        // 検索結果が表示されることを確認
        composeTestRule.onNodeWithText("ドラゴンナイト").assertIsDisplayed()
        
        // 検索結果に含まれないカードが表示されないことを確認
        composeTestRule.onNodeWithText("ゴブリン").assertDoesNotExist()
        
        // 検索ボックスをクリア
        composeTestRule.onNodeWithTag("SearchField").performTextClearance()
        
        // すべてのカードが再表示されることを確認
        composeTestRule.onNodeWithText("ドラゴンナイト").assertIsDisplayed()
        composeTestRule.onNodeWithText("ゴブリン").assertIsDisplayed()
    }
    
    @Test
    fun testSortFunctionality() {
        // ソートボタンをクリック
        composeTestRule.onNodeWithContentDescription("ソートオプション").performClick()
        
        // ソートオプションが表示されることを確認
        composeTestRule.onNodeWithText("名前順").assertIsDisplayed()
        composeTestRule.onNodeWithText("コスト順").assertIsDisplayed()
        composeTestRule.onNodeWithText("攻撃力順").assertIsDisplayed()
        composeTestRule.onNodeWithText("防御力順").assertIsDisplayed()
        
        // コスト順を選択
        composeTestRule.onNodeWithText("コスト順").performClick()
        
        // カードがコスト順にソートされることを確認
        // 最初に表示されるカードがコスト1のゴブリンであることを確認
        composeTestRule.onAllNodesWithTag("CardItem")[0].assertTextContains("ゴブリン")
    }
}
