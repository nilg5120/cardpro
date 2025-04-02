package com.example.cardpro.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cardpro.model.DeckCardLocation
import kotlinx.coroutines.flow.Flow

/**
 * デッキとカードの関連付け情報のデータアクセスオブジェクト
 */
@Dao
interface DeckCardLocationDao {
    /**
     * デッキとカードの関連付けを追加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeckCardLocation(deckCardLocation: DeckCardLocation)
    
    /**
     * 複数のデッキとカードの関連付けを追加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeckCardLocations(deckCardLocations: List<DeckCardLocation>)
    
    /**
     * 特定のデッキに含まれるカードの関連付けをすべて取得
     */
    @Query("SELECT * FROM deck_card_locations WHERE deckId = :deckId")
    fun getDeckCardLocations(deckId: String): Flow<List<DeckCardLocation>>
    
    /**
     * 特定のデッキに含まれるカードIDをすべて取得
     */
    @Query("SELECT DISTINCT cardId FROM deck_card_locations WHERE deckId = :deckId")
    suspend fun getCardIdsInDeck(deckId: String): List<String>

    /**
     * 特定のデッキと特定のカードの関連付けを削除
     */
    @Query("DELETE FROM deck_card_locations WHERE deckId = :deckId AND cardId = :cardId")
    suspend fun deleteDeckCardLocationsForCard(deckId: String, cardId: String)
    
    /**
     * 特定のデッキと特定のカードの特定の保管場所の関連付けを削除
     */
    @Query("DELETE FROM deck_card_locations WHERE deckId = :deckId AND cardId = :cardId AND location = :location")
    suspend fun deleteDeckCardLocation(deckId: String, cardId: String, location: String)
    
    /**
     * 特定のデッキのすべての関連付けを削除
     */
    @Query("DELETE FROM deck_card_locations WHERE deckId = :deckId")
    suspend fun deleteAllDeckCardLocations(deckId: String)
    
    /**
     * 特定のカードのすべての関連付けを削除
     */
    @Query("DELETE FROM deck_card_locations WHERE cardId = :cardId")
    suspend fun deleteAllCardLocations(cardId: String)
    
    /**
     * デッキとカードの関連付けを削除
     */
    @Delete
    suspend fun deleteDeckCardLocation(deckCardLocation: DeckCardLocation)
}
