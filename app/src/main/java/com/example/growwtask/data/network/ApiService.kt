package com.example.growwtask.data.network

import android.util.Log
import com.example.growwtask.BuildConfig
import com.example.growwtask.data.model.GetTopGainersAndLosersResponse
import com.example.growwtask.data.model.StockSymbolDetailsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath


class ApiService(private val client: HttpClient) {

    suspend fun getTopGainersAndLosers(): GetTopGainersAndLosersResponse =
        executeRequest(
            function = "TOP_GAINERS_LOSERS"
        )

    suspend fun getStockSymbolInformation(symbol: String): StockSymbolDetailsResponse =
        executeRequest(
            function = "OVERVIEW",
            parameters = mapOf("symbol" to symbol)
        )

    suspend fun getStockPriceHistory(symbol: String): String =
        executeRequest(
            function = "TIME_SERIES_DAILY",
            parameters = mapOf("symbol" to symbol)
        )

    suspend fun symbolSearch(keywords: String): String =
        executeRequest(
            function = "SYMBOL_SEARCH",
            parameters = mapOf("keywords" to keywords)
        )

    suspend fun getQuote(symbol: String): String =
        executeRequest(
            function = "GLOBAL_QUOTE",
            parameters = mapOf("keywords" to symbol)
        )


    private suspend inline fun <reified T> executeRequest(
        function: String,
        parameters: Map<String, String> = emptyMap()
    ): T {
        val response = client.get {
            url {
                host = "www.alphavantage.co"
                protocol = URLProtocol.HTTPS
                encodedPath = "/query"
                this.parameters.append("function", function)
                this.parameters.append("apikey", BuildConfig.API_KEY)

                parameters.forEach { (key, value) ->
                    this.parameters.append(key, value)
                }
            }
        }

        if (BuildConfig.DEBUG) {
            Log.d("ApiService", "Request for $function: $response")
            Log.d("ApiService", "Response body: ${response.body<T>()}")
        }

        return response.body()
    }
}
