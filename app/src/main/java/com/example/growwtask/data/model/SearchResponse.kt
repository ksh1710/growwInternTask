package com.example.growwtask.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val bestMatches: List<StockSearchResult>
)

@Serializable
data class StockSearchResult(
    val symbol: String,
    val name: String,
    val type: String,
    val region: String,
    val marketOpen: String,
    val marketClose: String,
    val timezone: String,
    val currency: String,
    val matchScore: String
)