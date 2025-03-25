package com.example.cardpro.model

/**
 * カードの情報を表すデータクラス
 */
data class CardInfo(
    val name: String,
    val cost: Int,
    val attack: Int,
    val defense: Int,
    val rarity: String
)
