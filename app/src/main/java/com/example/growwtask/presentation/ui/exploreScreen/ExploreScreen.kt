package com.example.growwtask.presentation.ui.exploreScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.growwtask.presentation.common.CustomTopAppBar
import com.example.growwtask.presentation.navigation.Destinations
import com.example.growwtask.presentation.ui.exploreScreen.components.TopGainersSection
import com.example.growwtask.presentation.ui.exploreScreen.components.TopLosersSection
import com.example.growwtask.viewmodel.ExploreViewModel
import androidx.compose.runtime.getValue
import com.example.growwtask.presentation.common.CustomSearchBar
import com.example.growwtask.presentation.ui.exploreScreen.components.RecentlySearchedSection
import com.example.growwtask.utils.UiState


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    vm: ExploreViewModel = hiltViewModel(),
    navController: NavController,
    navigateToViewAll: (String) -> Unit
) {

    val topLosersState by vm.topLosersState.collectAsState()
    val topGainersState by vm.topGainersState.collectAsState()
    val isSearchActive by vm.isSearchActive.collectAsState()
    val recentlySearchedStocks by vm.recentlySearchedStocks.collectAsState()

    Scaffold(topBar = {
        Column {
            if (!isSearchActive) {
                CustomTopAppBar(title = "Stocks App")
            }
            CustomSearchBar(
                onStockSelected = { symbol ->
                    navController.navigate(Destinations.ProductScreen.createRoute(symbol))
                }
            )
        }
    }) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                RecentlySearchedSection(
                    recentStocks = recentlySearchedStocks,
                    onStockClick = { symbol ->
                        navController.navigate(Destinations.ProductScreen.createRoute(symbol))
                    },
                    onViewAllClick = {
                        navController.navigate(Destinations.RecentlySearchedViewAllScreen.route)
                    }
                )
            }
            item {
                when (val state = topGainersState) {
                    is UiState.Loading -> {
                        TopGainersSection(
                            onViewAllClick = {},
                            navController = navController,
                            isLoading = true
                        )
                    }

                    is UiState.Success -> {
                        TopGainersSection(
                            isLoading = false,
                            navController = navController,
                            topGainers = state.data!!,
                            onViewAllClick = {
                                navigateToViewAll("gainers")
                            }
                        )
                    }

                    is UiState.Error -> {
                        Text(state.message)
                    }

                    is UiState.Idle -> {}
                }
            }
            item {
                when (val state = topLosersState) {
                    is UiState.Loading -> {
                        TopLosersSection(
                            onViewAllClick = {},
                            isLoading = true,
                            navController = navController
                        )
                    }

                    is UiState.Success -> {
                        TopLosersSection(
                            isLoading = false,
                            topLosers = state.data!!,
                            navController = navController,
                            onViewAllClick = {
                                navigateToViewAll("losers")
                            }
                        )
                    }

                    is UiState.Error -> {
                        Text(state.message)
                    }

                    is UiState.Idle -> {}
                }
            }
        }
    }
}
