package com.example.growwtask.utils

data class CacheEntry<T>(
    val data: T,
    val timestamp: Long = System.currentTimeMillis()
)

