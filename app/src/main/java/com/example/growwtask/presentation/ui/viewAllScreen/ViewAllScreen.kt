package com.example.growwtask.presentation.ui.viewAllScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.growwtask.presentation.common.CustomTopAppBar
import com.example.growwtask.viewmodel.ExploreViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.example.growwtask.data.model.StockItem
import com.example.growwtask.presentation.common.EmptyState
import com.example.growwtask.presentation.ui.exploreScreen.components.StockCard
import com.example.growwtask.utils.UiState
import com.example.growwtask.R


@Composable
fun ViewAllScreen(
    type: String,
    modifier: Modifier = Modifier,
    navigateToProductScreen:(String)-> Unit,
    vm: ExploreViewModel = hiltViewModel()
) {
    val topLosersState by vm.topLosersState.collectAsState()
    val topGainersState by vm.topGainersState.collectAsState()

    val title = if (type == "gainers") "Top Gainers" else "Top Losers"

    Scaffold(topBar = { CustomTopAppBar(title = title) }) { paddingValues ->
        when {
            type == "gainers" -> {
                when (val state = topGainersState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        if (state.data.isNullOrEmpty()) {
                            EmptyState(message = "No top gainers available")
                        } else {
                            LazyVerticalGrid(
                                modifier = modifier.padding(paddingValues),
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(8.dp)
                            ) {
                                items(state.data.size) { index ->
                                    val item = state.data[index]
                                    StockCard(
                                        stock = StockItem(
                                            name = item.ticker,
                                            price = item.price,
                                            icon = R.drawable.ic_launcher_foreground
                                        ),
                                        onCardClick = {
                                            navigateToProductScreen(item.ticker)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    is UiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(state.message)
                        }
                    }

                    is UiState.Idle -> {
                    }
                }
            }
            else -> {
                when (val state = topLosersState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        if (state.data.isNullOrEmpty()) {
                            EmptyState(message = "No top losers available")
                        } else {
                            LazyVerticalGrid(
                                modifier = modifier.padding(paddingValues),
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(8.dp)
                            ) {
                                items(state.data.size) { index ->
                                    val item = state.data[index]
                                    StockCard(
                                        stock = StockItem(
                                            name = item.ticker,
                                            price = item.price,
                                            icon = R.drawable.ic_launcher_foreground
                                        ),
                                        onCardClick = {
                                      navigateToProductScreen(item.ticker)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    is UiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(state.message)
                        }
                    }

                    is UiState.Idle -> {
                    }
                }
            }
        }
    }
}