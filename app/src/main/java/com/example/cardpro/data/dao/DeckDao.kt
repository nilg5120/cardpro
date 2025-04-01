package com.example.cardpro.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.cardpro.data.relation.DeckWithCards
import com.example.cardpro.model.DeckInfo
import kotlinx.coroutines.flow.Flow

/**
 * デッキ情報のデータアクセスオブジェクト
 */
@Dao
interface DeckDao {
    /**
     * すべてのデッキを取得
     */
    @Query("SELECT * FROM decks ORDER BY name ASC")
    fun getAllDecks(): Flow<List<DeckInfo>>
    
    /**
     * 特定のIDのデッキを取得
     */
    @Query("SELECT * FROM decks WHERE id = :deckId")
    suspend fun getDeckById(deckId: String): DeckInfo?
    
    /**
     * デッキを追加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: DeckInfo): Long
    
    /**
     * 複数のデッキを追加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDecks(decks: List<DeckInfo>)
    
    /**
     * デッキを更新
     */
    @Update
    suspend fun updateDeck(deck: DeckInfo)
    
    /**
     * デッキを削除
     */
    @Delete
    suspend fun deleteDeck(deck: DeckInfo)
    
    /**
     * すべてのデッキを削除
     */
    @Query("DELETE FROM decks")
    suspend fun deleteAllDecks()
    
    /**
     * 特定のデッキタイプのデッキを取得
     */
    @Query("SELECT * FROM decks WHERE deckType = :deckType ORDER BY name ASC")
    fun getDecksByType(deckType: String): Flow<List<DeckInfo>>
    
    /**
     * デッキとそれに含まれるカードを取得
     */
    @Transaction
    @Query("SELECT * FROM decks WHERE id = :deckId")
    suspend fun getDeckWithCards(deckId: String): DeckWithCards?
    
    /**
     * すべてのデッキとそれに含まれるカードを取得
     */
    @Transaction
    @Query("SELECT * FROM decks ORDER BY name ASC")
    fun getAllDecksWithCards(): Flow<List<DeckWithCards>>
}
