package com.example.cardpro.data.repository

import com.example.cardpro.data.dao.CardDao
import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.GroupedCardInfo
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
     * すべてのカードを取得 (同じ名前のカードはまとめ、枚数も取得)
     */
    fun getAllCardsGroupedByName(): Flow<List<GroupedCardInfo>> = cardDao.getAllCardsGroupedByName()
    
    /**
     * 特定のIDのカードを取得
     */
    suspend fun getCardById(cardId: String): CardInfo? = cardDao.getCardById(cardId)

    /**
     * 特定の名前のカード複数を取得
     */
    suspend fun getCardsByName(name: String): List<CardInfo> = cardDao.getCardsByName(name)
    
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
}
