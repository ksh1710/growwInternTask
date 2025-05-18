package com.example.growwtask.presentation.ui.productScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.growwtask.data.model.StockPriceData
import com.example.growwtask.data.model.StockSymbolDetailsResponse
import com.example.growwtask.presentation.common.CustomTopAppBar
import com.example.growwtask.presentation.common.EmptyState
import com.example.growwtask.utils.UiState
import com.example.growwtask.viewmodel.StockDetailsViewModel
import com.example.growwtask.presentation.ui.productScreen.components.StockDetailsContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    symbol: String,
    viewModel: StockDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(symbol) {
        viewModel.fetchStockDetails(symbol)
        Log.d("idk", "ProductScreen: $symbol launched effect called")
    }

    val stockDetailsState by viewModel.stockDetailsState.collectAsState()
    val stockPriceDataState by viewModel.stockPriceData.collectAsState()

    val refreshing = remember { mutableStateOf(false) }
    val ctx = LocalContext.current
    val state = rememberPullToRefreshState()
    LaunchedEffect(refreshing.value) {
        if (refreshing.value) {
            Toast.makeText(ctx, "refreshing", Toast.LENGTH_SHORT).show()
            viewModel.refreshStockDetails(symbol)
            refreshing.value = false
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = symbol
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = refreshing.value,
            state = state,
            onRefresh = {
                refreshing.value = true
            }
        ) {
            when {
                stockDetailsState is UiState.Loading || stockPriceDataState is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                stockDetailsState is UiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("some error occurred")
                    }
                }

                stockDetailsState is UiState.Success && stockPriceDataState is UiState.Success -> {
                    val stockDetails =
                        (stockDetailsState as UiState.Success<StockSymbolDetailsResponse>).data
                    val priceData =
                        (stockPriceDataState as UiState.Success<List<StockPriceData>>).data

                    if (stockDetails == null) {
                        EmptyState(message = "No stock details available")
                    } else {
                        StockDetailsContent(
                            stockDetails = stockDetails,
                            priceData = priceData ?: emptyList(),
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}
