package com.example.cardpro.model

import androidx.room.Entity
import androidx.room.Ignore
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
    val memo: String = "", // カードに関するメモ
    @Ignore
    val count: Int = 1 // カードの枚数（デフォルトは1枚）
) {
    // Room用のセカンダリコンストラクタ（@Ignoreフィールドを除外）
    constructor(
        id: String,
        name: String,
        cost: Int,
        attack: Int,
        defense: Int,
        rarity: String,
        location: String,
        memo: String
    ) : this(id, name, cost, attack, defense, rarity, location, memo, 1)
}
