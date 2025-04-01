package com.example.cardpro

import android.app.Application
import com.example.cardpro.data.database.CardProDatabase
import com.example.cardpro.data.repository.CardRepository
import com.example.cardpro.data.repository.DeckRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * CardProアプリケーションクラス
 */
class CardProApplication : Application() {
    // アプリケーションスコープのコルーチンスコープ
    private val applicationScope = CoroutineScope(SupervisorJob())
    
    // データベースのインスタンス
    val database by lazy { CardProDatabase.getDatabase(this) }
    
    // リポジトリのインスタンス
    val cardRepository by lazy { CardRepository(database.cardDao()) }
    val deckRepository by lazy { DeckRepository(database.deckDao(), database.deckCardLocationDao()) }
}
