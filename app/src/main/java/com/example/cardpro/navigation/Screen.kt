package com.example.cardpro.navigation

/**
 * アプリケーション内の画面を表す列挙型
 */
sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object DeckList : Screen("deck_list")
    object CardList : Screen("card_list")
    object DeckDetail : Screen("deck_detail/{deckId}") {
        fun createRoute(deckId: String): String {
            return "deck_detail/$deckId"
        }
    }
}
