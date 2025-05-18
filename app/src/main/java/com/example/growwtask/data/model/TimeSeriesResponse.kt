package com.example.growwtask.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TimeSeriesResponse(
    val metaData: MetaData,
    val timeSeries: Map<String, DailyData>
)

@Serializable
data class MetaData(
    val information: String,
    val symbol: String,
    val lastRefreshed: String,
    val outputSize: String,
    val timeZone: String
)

@Serializable
data class DailyData(
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val adjustedClose: String,
    val volume: String,
    val dividendAmount: String,
    val splitCoefficient: String
)