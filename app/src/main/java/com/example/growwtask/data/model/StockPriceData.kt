package com.example.growwtask.data.model



data class StockPriceData(
    val date: String,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Long,
)