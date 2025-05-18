package com.example.growwtask.data.model

data class RecentlySearchedStock(
    val symbol: String,
    val name: String,
    val price: String,
    val timestamp: Long = System.currentTimeMillis()
)