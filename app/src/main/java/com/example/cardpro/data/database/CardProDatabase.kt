package com.example.cardpro.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cardpro.data.dao.CardDao
import com.example.cardpro.data.dao.DeckCardLocationDao
import com.example.cardpro.data.dao.DeckDao
import com.example.cardpro.model.CardInfo
import com.example.cardpro.model.DeckCardLocation
import com.example.cardpro.model.DeckInfo

/**
 * CardProアプリケーションのRoomデータベース
 */
@Database(
    entities = [CardInfo::class, DeckInfo::class, DeckCardLocation::class],
    version = 1,
    exportSchema = false
)
abstract class CardProDatabase : RoomDatabase() {
    
    /**
     * カード情報のDAOを取得
     */
    abstract fun cardDao(): CardDao
    
    /**
     * デッキ情報のDAOを取得
     */
    abstract fun deckDao(): DeckDao
    
    /**
     * デッキとカードの関連付け情報のDAOを取得
     */
    abstract fun deckCardLocationDao(): DeckCardLocationDao
    
    companion object {
        // シングルトンインスタンス
        @Volatile
        private var INSTANCE: CardProDatabase? = null
        
        /**
         * データベースインスタンスを取得
         */
        fun getDatabase(context: Context): CardProDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardProDatabase::class.java,
                    "cardpro_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
