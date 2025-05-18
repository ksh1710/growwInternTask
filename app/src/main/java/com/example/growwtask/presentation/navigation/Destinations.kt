package com.example.growwtask.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class Destinations(var route: String) {
    data object ExploreScreen : Destinations("ExploreScreen")
    data object ProductScreen : Destinations("ProductScreen/{symbol}"){
        fun createRoute(symbol: String): String {
            return "ProductScreen/$symbol"
        }
    }
    data object ViewAllScreen : Destinations("ViewAllScreen/{type}") {
        fun createRoute(type: String): String {
            return "ViewAllScreen/$type"
        }
    }
   data object RecentlySearchedViewAllScreen: Destinations("RecentlySearchedViewAllScreen")
}
