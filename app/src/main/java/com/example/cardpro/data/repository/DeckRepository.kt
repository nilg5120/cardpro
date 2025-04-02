package com.example.cardpro.data.repository

import com.example.cardpro.data.dao.DeckCardLocationDao
import com.example.cardpro.data.dao.DeckDao
import com.example.cardpro.data.relation.DeckWithCards
import com.example.cardpro.model.DeckCardLocation
import com.example.cardpro.model.DeckInfo
import kotlinx.coroutines.flow.Flow

/**
 * デッキ情報のリポジトリ
 */
class DeckRepository(
    private val deckDao: DeckDao,
    private val deckCardLocationDao: DeckCardLocationDao
) {
    
    /**
     * すべてのデッキを取得
     */
    fun getAllDecks(): Flow<List<DeckInfo>> = deckDao.getAllDecks()

    /**
     * デッキを追加
     */
    suspend fun insertDeck(deck: DeckInfo): Long = deckDao.insertDeck(deck)
    
    /**
     * 複数のデッキを追加
     */
    suspend fun insertDecks(decks: List<DeckInfo>) = deckDao.insertDecks(decks)
    
    /**
     * デッキを更新
     */
    suspend fun updateDeck(deck: DeckInfo) = deckDao.updateDeck(deck)
    
    /**
     * デッキを削除
     */
    suspend fun deleteDeck(deck: DeckInfo) {
        // デッキとカードの関連付けを削除
        deckCardLocationDao.deleteAllDeckCardLocations(deck.id)
        // デッキを削除
        deckDao.deleteDeck(deck)
    }

    /**
     * デッキとそれに含まれるカードを取得
     */
    suspend fun getDeckWithCards(deckId: String): DeckWithCards? = deckDao.getDeckWithCards(deckId)

    /**
     * デッキにカードを追加
     */
    suspend fun addCardToDeck(deckId: String, cardId: String, location: String) {
        val deckCardLocation = DeckCardLocation(deckId, cardId, location)
        deckCardLocationDao.insertDeckCardLocation(deckCardLocation)
    }
    
    /**
     * デッキから特定のカードをすべて削除
     */
    suspend fun removeCardFromDeck(deckId: String, cardId: String) {
        deckCardLocationDao.deleteDeckCardLocationsForCard(deckId, cardId)
    }
    
    /**
     * デッキから特定のカードの特定の保管場所を削除
     */
    suspend fun removeCardLocationFromDeck(deckId: String, cardId: String, location: String) {
        deckCardLocationDao.deleteDeckCardLocation(deckId, cardId, location)
    }
    
    /**
     * 特定のデッキに含まれるカードの関連付けをすべて取得
     */
    fun getDeckCardLocations(deckId: String) = deckCardLocationDao.getDeckCardLocations(deckId)

}
