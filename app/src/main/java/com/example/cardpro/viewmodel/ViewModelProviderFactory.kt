package com.example.cardpro.viewmodel

import android.content.Context
import com.example.cardpro.CardProApplication

/**
 * ViewModelProviderFactoryを提供するユーティリティクラス
 */
object ViewModelProviderFactory {
    /**
     * CardViewModelFactoryを取得する
     */
    fun getCardViewModelFactory(context: Context): CardViewModel.CardViewModelFactory {
        val application = context.applicationContext as CardProApplication
        return CardViewModel.CardViewModelFactory(application.cardRepository)
    }
    
    /**
     * DeckViewModelFactoryを取得する
     */
    fun getDeckViewModelFactory(context: Context): DeckViewModel.DeckViewModelFactory {
        val application = context.applicationContext as CardProApplication
        return DeckViewModel.DeckViewModelFactory(
            application.deckRepository,
            application.cardRepository
        )
    }
}
