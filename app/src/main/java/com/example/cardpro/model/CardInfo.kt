package com.example.cardpro.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * カードの情報を表すデータクラス
 */
@Entity(tableName = "cards")
data class CardInfo(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(), // カードの一意のID
    val name: String,
    val cost: Int,
    val attack: Int,
    val defense: Int,
    val rarity: String,
    val location: String = "", // カードの保管場所
    val memo: String = "" // カードに関するメモ
)
