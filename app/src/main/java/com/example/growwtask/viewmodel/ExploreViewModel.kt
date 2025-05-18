package com.example.growwtask.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.growwtask.data.model.RecentlySearchedStock
import com.example.growwtask.data.model.StockSearchResult
import com.example.growwtask.data.model.TopGainer
import com.example.growwtask.data.model.TopLoser
import com.example.growwtask.data.repository.StockDataRepository
import com.example.growwtask.utils.StockPreferencesManager
import com.example.growwtask.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: StockDataRepository,
    private val preferencesManager: StockPreferencesManager
) : ViewModel() {

    private val _recentlySearchedStocks = MutableStateFlow<List<RecentlySearchedStock>>(emptyList())
    val recentlySearchedStocks: StateFlow<List<RecentlySearchedStock>> = _recentlySearchedStocks

    private val _topLosersState =
        MutableStateFlow<UiState<List<TopLoser>>>(UiState.Idle)
    val topLosersState: StateFlow<UiState<List<TopLoser>>> = _topLosersState

    private val _topGainersState =
        MutableStateFlow<UiState<List<TopGainer>>>(UiState.Idle)
    val topGainersState: StateFlow<UiState<List<TopGainer>>> = _topGainersState

    init {
        fetchTopGainersAndLosers()
        fetchRecentlySearchedStocks()
    }

    private fun fetchRecentlySearchedStocks() {
        viewModelScope.launch {
            preferencesManager.getRecentSearches()
                .collect { stocks ->
                    _recentlySearchedStocks.value = stocks
                }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun addToRecentSearches(stockSearchResult: StockSearchResult) {
        viewModelScope.launch {
            val priceResult = repository.getLatestPrice(stockSearchResult.symbol)
            val price = priceResult.getOrNull() ?: "N/A"

            val recentStock = RecentlySearchedStock(
                symbol = stockSearchResult.symbol,
                name = stockSearchResult.name,
                price = price
            )

            preferencesManager.saveRecentSearch(recentStock)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            preferencesManager.clearRecentSearches()
        }
    }
    fun fetchTopGainersAndLosers() {
        viewModelScope.launch {
            _topLosersState.value = UiState.Loading
            _topGainersState.value = UiState.Loading
            try {
                val response = repository.getTopGainersAndLosers()
                Log.d("idk", "fetchTopGainersAndLosers: ${response.getOrNull()} ")
                if (response.isFailure) {
                    _topLosersState.value =
                        UiState.Error(
                            response.exceptionOrNull()?.message ?: "Unknown error occurred"
                        )
                    return@launch
                }
                _topLosersState.value = UiState.Success(response.getOrNull()?.top_losers)
                _topGainersState.value = UiState.Success(response.getOrNull()?.top_gainers)
            } catch (e: Exception) {
                _topLosersState.value = UiState.Error(e.message ?: "Unknown error occurred")
                _topGainersState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<UiState<List<StockSearchResult>>>(UiState.Idle)
    val searchResults: StateFlow<UiState<List<StockSearchResult>>> = _searchResults

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive

    private var searchJob: Job? = null

    fun setSearchQuery(query: String) {
        _searchQuery.value = query

        if (query.isEmpty()) {
            _searchResults.value = UiState.Idle
            _isSearchActive.value = false
            return
        }

        _isSearchActive.value = true

        searchJob?.cancel()

        // Debouncing to avoid too many API calls
        searchJob = viewModelScope.launch {
            delay(300)
            _searchResults.value = UiState.Loading

            try {
                val result = repository.searchStocks(query)
                if (result.isSuccess) {
                    _searchResults.value = UiState.Success(result.getOrNull() ?: emptyList())
                } else {
                    _searchResults.value = UiState.Error(
                        result.exceptionOrNull()?.message ?: "Search failed"
                    )
                }
            } catch (e: Exception) {
                _searchResults.value = UiState.Error(e.message ?: "Search failed")
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = UiState.Idle
        _isSearchActive.value = false
        searchJob?.cancel()
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            clearSearch()
        }
    }

}