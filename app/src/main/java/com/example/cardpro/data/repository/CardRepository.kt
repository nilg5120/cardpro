package com.example.cardpro.data.repository

import com.example.cardpro.data.dao.CardDao
import com.example.cardpro.data.relation.CardWithDecks
import com.example.cardpro.model.CardInfo
import kotlinx.coroutines.flow.Flow

/**
 * カード情報のリポジトリ
 */
class CardRepository(private val cardDao: CardDao) {
    
    /**
     * すべてのカードを取得
     */
    fun getAllCards(): Flow<List<CardInfo>> = cardDao.getAllCards()
    
    /**
     * 特定のIDのカードを取得
     */
    suspend fun getCardById(cardId: String): CardInfo? = cardDao.getCardById(cardId)
    
    /**
     * カードを追加
     */
    suspend fun insertCard(card: CardInfo) = cardDao.insertCard(card)
    
    /**
     * 複数のカードを追加
     */
    suspend fun insertCards(cards: List<CardInfo>) = cardDao.insertCards(cards)
    
    /**
     * カードを更新
     */
    suspend fun updateCard(card: CardInfo) = cardDao.updateCard(card)
    
    /**
     * カードを削除
     */
    suspend fun deleteCard(card: CardInfo) = cardDao.deleteCard(card)
    
    /**
     * すべてのカードを削除
     */
    suspend fun deleteAllCards() = cardDao.deleteAllCards()
    
    /**
     * カードとそれが含まれるデッキを取得
     */
    suspend fun getCardWithDecks(cardId: String): CardWithDecks? = cardDao.getCardWithDecks(cardId)
    
    /**
     * すべてのカードとそれが含まれるデッキを取得
     */
    fun getAllCardsWithDecks(): Flow<List<CardWithDecks>> = cardDao.getAllCardsWithDecks()
    
    /**
     * レアリティでカードを検索
     */
    fun getCardsByRarity(rarity: String): Flow<List<CardInfo>> = cardDao.getCardsByRarity(rarity)
    
    /**
     * コストでカードを検索
     */
    fun getCardsByCost(cost: Int): Flow<List<CardInfo>> = cardDao.getCardsByCost(cost)
}
