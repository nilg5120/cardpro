package com.example.cardpro.model

data class GroupedCardInfo(
    val name: String,
    val cost: Int,
    val attack: Int,
    val defense: Int,
    val rarity: String,
    val location: String,
    val memo: String,
    val count: Int
)
