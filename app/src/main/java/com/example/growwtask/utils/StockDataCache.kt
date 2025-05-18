package com.example.growwtask.utils

import com.example.growwtask.data.model.StockPriceData
import com.example.growwtask.data.model.StockSymbolDetailsResponse

class StockDataCache {
    private val stockOverviewCache = mutableMapOf<String, CacheEntry<StockSymbolDetailsResponse>>()
    private val stockPriceHistoryCache = mutableMapOf<String, CacheEntry<List<StockPriceData>>>()
    
    private val cacheExpirationTime = 15 * 60 * 1000L
    
    fun getStockOverview(symbol: String): StockSymbolDetailsResponse? {
        val cacheEntry = stockOverviewCache[symbol] ?: return null
        
        if (System.currentTimeMillis() - cacheEntry.timestamp > cacheExpirationTime) {
            stockOverviewCache.remove(symbol)
            return null
        }
        
        return cacheEntry.data
    }
    
    fun cacheStockOverview(symbol: String, data: StockSymbolDetailsResponse) {
        stockOverviewCache[symbol] = CacheEntry(data)
    }
    
    fun getStockPriceHistory(symbol: String): List<StockPriceData>? {
        val cacheEntry = stockPriceHistoryCache[symbol] ?: return null
        
        if (System.currentTimeMillis() - cacheEntry.timestamp > cacheExpirationTime) {
            stockPriceHistoryCache.remove(symbol)
            return null
        }
        
        return cacheEntry.data
    }
    
    fun cacheStockPriceHistory(symbol: String, data: List<StockPriceData>) {
        stockPriceHistoryCache[symbol] = CacheEntry(data)
    }
}
