package com.example.cardpro.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.cardpro.model.DeckInfo
import com.example.cardpro.viewmodel.DeckViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeckListScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    private lateinit var viewModel: DeckViewModel
    
    @Before
    fun setup() {
        viewModel = DeckViewModel()
        
        // DeckListScreenをセットアップ
        composeTestRule.setContent {
            DeckListScreen(
                viewModel = viewModel,
                onDeckClick = {},
                onNavigateToMenu = {}
            )
        }
    }
    
    @Test
    fun testDeckListDisplayed() {
        // 初期状態で5つのデッキが表示されていることを確認
        composeTestRule.onNodeWithText("スタンダードデッキ").assertIsDisplayed()
        composeTestRule.onNodeWithText("アグロデッキ").assertIsDisplayed()
        composeTestRule.onNodeWithText("コントロールデッキ").assertIsDisplayed()
        composeTestRule.onNodeWithText("ミッドレンジデッキ").assertIsDisplayed()
        composeTestRule.onNodeWithText("コンボデッキ").assertIsDisplayed()
    }
    
    @Test
    fun testDeckDescriptionsDisplayed() {
        // デッキの説明が表示されていることを確認
        composeTestRule.onNodeWithText("基本的なカードで構成された初心者向けデッキ").assertIsDisplayed()
        composeTestRule.onNodeWithText("素早く攻撃するための低コストカードを中心としたデッキ").assertIsDisplayed()
        composeTestRule.onNodeWithText("相手の動きを制限しながら有利に進めるデッキ").assertIsDisplayed()
    }
    
    @Test
    fun testDeckTypeDisplayed() {
        // デッキのタイプが表示されていることを確認
        composeTestRule.onNodeWithText("バランス型").assertIsDisplayed()
        composeTestRule.onNodeWithText("攻撃型").assertIsDisplayed()
        composeTestRule.onNodeWithText("防御型").assertIsDisplayed()
        composeTestRule.onNodeWithText("コンボ型").assertIsDisplayed()
    }
    
    @Test
    fun testAddButtonDisplayed() {
        // 追加ボタンが表示されていることを確認
        composeTestRule.onNodeWithContentDescription("デッキを追加").assertIsDisplayed()
    }
    
    @Test
    fun testAddButtonClickShowsDialog() {
        // 追加ボタンをクリック
        composeTestRule.onNodeWithContentDescription("デッキを追加").performClick()
        
        // ダイアログが表示されることを確認
        composeTestRule.onNodeWithText("デッキを追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
        composeTestRule.onNodeWithText("追加").assertIsDisplayed()
    }
    
    @Test
    fun testEditButtonClickShowsDialog() {
        // 編集ボタンをクリック
        composeTestRule.onAllNodesWithContentDescription("デッキを編集")[0].performClick()
        
        // ダイアログが表示されることを確認
        composeTestRule.onNodeWithText("デッキを編集").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
        composeTestRule.onNodeWithText("更新").assertIsDisplayed()
    }
    
    @Test
    fun testDeleteButtonClickShowsDialog() {
        // 削除ボタンをクリック
        composeTestRule.onAllNodesWithContentDescription("デッキを削除")[0].performClick()
        
        // ダイアログが表示されることを確認
        composeTestRule.onNodeWithText("デッキを削除").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
        composeTestRule.onNodeWithText("削除").assertIsDisplayed()
    }
    
    @Test
    fun testAddNewDeck() {
        // 追加ボタンをクリック
        composeTestRule.onNodeWithContentDescription("デッキを追加").performClick()
        
        // ダイアログにデッキ情報を入力
        composeTestRule.onAllNodesWithTag("TextField")[0].performTextInput("テストデッキ")
        composeTestRule.onAllNodesWithTag("TextField")[1].performTextInput("テスト用デッキの説明")
        composeTestRule.onAllNodesWithTag("TextField")[2].performTextInput("テスト型")
        
        // 追加ボタンをクリック
        composeTestRule.onNodeWithText("追加").performClick()
        
        // 新しいデッキが追加されたことを確認
        composeTestRule.onNodeWithText("テストデッキ").assertIsDisplayed()
        composeTestRule.onNodeWithText("テスト用デッキの説明").assertIsDisplayed()
        composeTestRule.onNodeWithText("テスト型").assertIsDisplayed()
    }
}
