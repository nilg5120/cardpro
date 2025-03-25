package com.example.cardpro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cardpro.screens.CardListScreen
import com.example.cardpro.screens.DeckDetailScreen
import com.example.cardpro.screens.DeckListScreen
import com.example.cardpro.screens.MenuScreen

/**
 * アプリケーションのナビゲーショングラフ
 *
 * @param navController ナビゲーションコントローラー
 * @param startDestination 初期画面
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Menu.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // メニュー画面
        composable(route = Screen.Menu.route) {
            MenuScreen(
                onNavigateToDeckList = {
                    navController.navigate(Screen.DeckList.route)
                },
                onNavigateToCardList = {
                    navController.navigate(Screen.CardList.route)
                }
            )
        }
        
        // デッキ一覧画面
        composable(route = Screen.DeckList.route) {
            DeckListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onDeckClick = { deck ->
                    navController.navigate(Screen.DeckDetail.createRoute(deck.name))
                }
            )
        }
        
        // カード一覧画面
        composable(route = Screen.CardList.route) {
            CardListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // デッキ詳細画面
        composable(
            route = Screen.DeckDetail.route,
            arguments = listOf(
                navArgument("deckId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString("deckId") ?: ""
            DeckDetailScreen(
                deckId = deckId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
