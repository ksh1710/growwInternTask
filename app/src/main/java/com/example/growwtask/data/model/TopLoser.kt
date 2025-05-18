package com.example.growwtask.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TopLoser(
    val change_amount: String,
    val change_percentage: String,
    val price: String,
    val ticker: String,
    val volume: String
)