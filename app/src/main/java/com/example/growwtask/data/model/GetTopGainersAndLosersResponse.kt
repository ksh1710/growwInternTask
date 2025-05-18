package com.example.growwtask.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GetTopGainersAndLosersResponse(
    val last_updated: String? = null,
    val metadata: String? =null,
    val most_actively_traded: List<MostActivelyTraded>?=emptyList(),
    val top_gainers: List<TopGainer>?=emptyList(),
    val top_losers: List<TopLoser>?=emptyList()
)