package com.example.growwtask.data.repository

import android.util.Log
import com.example.growwtask.data.model.GetTopGainersAndLosersResponse
import com.example.growwtask.data.model.StockPriceData
import com.example.growwtask.data.model.StockSearchResult
import com.example.growwtask.data.model.StockSymbolDetailsResponse
import com.example.growwtask.data.network.ApiService
import com.example.growwtask.utils.StockDataCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class StockDataRepository @Inject constructor(
    private val apiService: ApiService,
    private val cache: StockDataCache
) {

    suspend fun getTopGainersAndLosers(): Result<GetTopGainersAndLosersResponse> {
        return try {
            val data = apiService.getTopGainersAndLosers()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStockOverview(symbol: String): Result<StockSymbolDetailsResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Check cache first
                val cachedData = cache.getStockOverview(symbol)
                if (cachedData != null) {
                    Log.d("StockDataRepository", "Returning cached overview data for $symbol")
                    return@withContext Result.success(cachedData)
                }

                val data = apiService.getStockSymbolInformation(symbol)
                cache.cacheStockOverview(symbol, data)
                Log.d("StockDataRepository", "Caching overview data for $symbol")
                Result.success(data)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getStockPriceHistory(symbol: String): Result<List<StockPriceData>> {
        return withContext(Dispatchers.IO) {
            try {
                val cachedData = cache.getStockPriceHistory(symbol)
                if (cachedData != null) {
                    Log.d("StockDataRepository", "Returning cached price data for $symbol")
                    return@withContext Result.success(cachedData)
                }

                val response = apiService.getStockPriceHistory(symbol)
                val priceData = parseTimeSeriesResponse(response)
                cache.cacheStockPriceHistory(symbol, priceData)
                Log.d("StockDataRepository", "Caching price data for $symbol")
                Result.success(priceData)
            } catch (e: Exception) {
                Log.e("StockDataRepository", "Error fetching stock price history", e)
                Result.failure(e)
            }
        }
    }

    private fun parseTimeSeriesResponse(jsonResponse: String): List<StockPriceData> {
        val priceDataList = mutableListOf<StockPriceData>()

        try {
            val jsonObject = JSONObject(jsonResponse)
            Log.d("idk", "parseTimeSeriesResponse: $jsonObject")
            val timeSeriesObj = jsonObject.getJSONObject("Time Series (Daily)")
            val dateKeys = timeSeriesObj.keys()

            while (dateKeys.hasNext()) {
                val date = dateKeys.next()
                val dailyData = timeSeriesObj.getJSONObject(date)

                val priceData = StockPriceData(
                    date = date,
                    open = dailyData.getString("1. open").toFloatOrNull() ?: 0f,
                    high = dailyData.getString("2. high").toFloatOrNull() ?: 0f,
                    low = dailyData.getString("3. low").toFloatOrNull() ?: 0f,
                    close = dailyData.getString("4. close").toFloatOrNull() ?: 0f,
                    volume = dailyData.getString("5. volume").toLongOrNull() ?: 0L
                )

                priceDataList.add(priceData)
            }
        } catch (e: Exception) {
            Log.e("StockDataRepository", "Error parsing time series data", e)
        }
        return priceDataList.sortedByDescending { it.date }
    }

    suspend fun searchStocks(query: String): Result<List<StockSearchResult>> {
        return try {
            if (query.length < 2) {
                return Result.success(emptyList())
            }

            val response = apiService.symbolSearch(keywords = query)
            val searchResults = parseSearchResponse(response)
            Result.success(searchResults)
        } catch (e: Exception) {
            Log.e("StockDataRepository", "Error searching stocks", e)
            Result.failure(e)
        }
    }

    private fun parseSearchResponse(jsonResponse: String): List<StockSearchResult> {
        val searchResults = mutableListOf<StockSearchResult>()

        try {
            val jsonObject = JSONObject(jsonResponse)
            val bestMatches = jsonObject.optJSONArray("bestMatches") ?: return emptyList()

            for (i in 0 until bestMatches.length()) {
                val match = bestMatches.getJSONObject(i)

                val result = StockSearchResult(
                    symbol = match.getString("1. symbol"),
                    name = match.getString("2. name"),
                    type = match.getString("3. type"),
                    region = match.getString("4. region"),
                    marketOpen = match.getString("5. marketOpen"),
                    marketClose = match.getString("6. marketClose"),
                    timezone = match.getString("7. timezone"),
                    currency = match.getString("8. currency"),
                    matchScore = match.getString("9. matchScore")
                )

                searchResults.add(result)
            }
        } catch (e: Exception) {
            Log.e("StockDataRepository", "Error parsing search response", e)
        }

        return searchResults
    }

    suspend fun getLatestPrice(symbol: String): Result<String> {
        return try {
            val response = apiService.getQuote(symbol)

            val jsonObject = JSONObject(response)
            val globalQuote = jsonObject.optJSONObject("Global Quote")

            if (globalQuote != null) {
                val price = globalQuote.optString("05. price", "N/A")
                Result.success(price)
            } else {
                Result.failure(Exception("Price data not available"))
            }
        } catch (e: Exception) {
            Log.e("StockDataRepository", "Error fetching latest price", e)
            Result.failure(e)
        }
    }

}