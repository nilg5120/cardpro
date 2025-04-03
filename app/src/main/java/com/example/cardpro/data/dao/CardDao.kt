package com.example.cardpro.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.cardpro.data.relation.CardWithDecks
import com.example.cardpro.model.CardInfo
import kotlinx.coroutines.flow.Flow

/**
 * カード情報のデータアクセスオブジェクト
 */
@Dao
interface CardDao {
    /**
     * すべてのカードを取得
     */
    @Query("SELECT * FROM cards ORDER BY name ASC")
    fun getAllCards(): Flow<List<CardInfo>>


    /**
     * すべてのカードを取得 (同じ名前のカードはまとめ、枚数も取得)
     */
    @Query("""
        SELECT *, COUNT(*) AS count 
        FROM cards 
        GROUP BY name 
        ORDER BY name ASC
    """)
    fun getAllCardsGroupedByName(): Flow<List<CardInfo>>

    /**
     * 特定のIDのカードを取得
     */
    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: String): CardInfo?
    
    /**
     * カードを追加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardInfo)
    
    /**
     * 複数のカードを追加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardInfo>)
    
    /**
     * カードを更新
     */
    @Update
    suspend fun updateCard(card: CardInfo)
    
    /**
     * カードを削除
     */
    @Delete
    suspend fun deleteCard(card: CardInfo)
    
    /**
     * すべてのカードを削除
     */
    @Query("DELETE FROM cards")
    suspend fun deleteAllCards()
    
    /**
     * カードとそれが含まれるデッキを取得
     */
    @Transaction
    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardWithDecks(cardId: String): CardWithDecks?
    
    /**
     * すべてのカードとそれが含まれるデッキを取得
     */
    @Transaction
    @Query("SELECT * FROM cards ORDER BY name ASC")
    fun getAllCardsWithDecks(): Flow<List<CardWithDecks>>
    
    /**
     * レアリティでカードを検索
     */
    @Query("SELECT * FROM cards WHERE rarity = :rarity ORDER BY name ASC")
    fun getCardsByRarity(rarity: String): Flow<List<CardInfo>>
    
    /**
     * コストでカードを検索
     */
    @Query("SELECT * FROM cards WHERE cost = :cost ORDER BY name ASC")
    fun getCardsByCost(cost: Int): Flow<List<CardInfo>>
}
