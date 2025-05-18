package com.example.growwtask.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.growwtask.data.model.StockPriceData
import com.example.growwtask.data.model.StockSymbolDetailsResponse
import com.example.growwtask.data.repository.StockDataRepository
import com.example.growwtask.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockDetailsViewModel @Inject constructor(
    private val repository: StockDataRepository
) : ViewModel() {

    private val _stockDetailsState =
        MutableStateFlow<UiState<StockSymbolDetailsResponse>>(UiState.Idle)
    val stockDetailsState: StateFlow<UiState<StockSymbolDetailsResponse>> = _stockDetailsState

    private val _stockPriceData = MutableStateFlow<UiState<List<StockPriceData>>>(UiState.Idle)
    val stockPriceData: StateFlow<UiState<List<StockPriceData>>> = _stockPriceData

    private val _allPriceData = MutableStateFlow<List<StockPriceData>>(emptyList())

    private val loadedSymbols = mutableSetOf<String>()


    fun fetchStockDetails(symbol: String, forceRefresh: Boolean = false) {

        if (!forceRefresh && symbol in loadedSymbols) {
            Log.d("StockDetailsViewModel", "Skipping fetch for $symbol as it's already loaded")
            return
        }

        viewModelScope.launch {
            _stockDetailsState.value = UiState.Loading
            _stockPriceData.value = UiState.Loading

            try {
                Log.d("idk", "fetchStockDetails: vm called ")
                val detailsDeferred = async { repository.getStockOverview(symbol) }
                val priceDataDeferred = async { repository.getStockPriceHistory(symbol) }

                val detailsResult = detailsDeferred.await()
                val priceDataResult = priceDataDeferred.await()
                Log.d("idk", "fetchStockDetails: ${detailsResult.getOrNull()}")
                Log.d("idk", "fetchStockDetails: ${priceDataResult.getOrNull()}")

                if (detailsResult.isSuccess) {
                    _stockDetailsState.value = UiState.Success(detailsResult.getOrNull())
                } else {
                    _stockDetailsState.value = UiState.Error(
                        detailsResult.exceptionOrNull()?.message ?: "Failed to fetch stock details"
                    )
                }
                if (priceDataResult.isSuccess) {
                    val priceData = priceDataResult.getOrNull() ?: emptyList<StockPriceData>()
                    _allPriceData.value = priceData
                    _stockPriceData.value = UiState.Success(priceData)
                } else {
                    _stockPriceData.value = UiState.Error(
                        priceDataResult.exceptionOrNull()?.message ?: "Failed to fetch price data"
                    )
                }

                loadedSymbols.add(symbol)


            } catch (e: Exception) {
                _stockDetailsState.value =
                    UiState.Error(e.message ?: "Failed to fetch stock details")
                _stockPriceData.value =
                    UiState.Error(e.message ?: "Failed to fetch stock price data")
            }
        }
    }

    fun refreshStockDetails(symbol: String) {
        fetchStockDetails(symbol, forceRefresh = true)
    }
}