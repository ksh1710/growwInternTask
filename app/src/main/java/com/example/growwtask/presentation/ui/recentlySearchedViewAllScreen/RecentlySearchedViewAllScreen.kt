package com.example.growwtask.presentation.ui.recentlySearchedViewAllScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.growwtask.presentation.common.CustomTopAppBar
import com.example.growwtask.presentation.navigation.Destinations
import com.example.growwtask.presentation.ui.exploreScreen.components.RecentlySearchedItem
import com.example.growwtask.viewmodel.ExploreViewModel
import androidx.compose.runtime.getValue

@Composable
fun RecentlySearchedViewAllScreen(
    navController: NavController,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val recentlySearchedStocks by viewModel.recentlySearchedStocks.collectAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Recently Searched",
            )
        },
        floatingActionButton = {
            if (recentlySearchedStocks.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { viewModel.clearRecentSearches() },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear History",
                        tint = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        if (recentlySearchedStocks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recently searched stocks",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(recentlySearchedStocks) { stock ->
                    RecentlySearchedItem(
                        stock = stock,
                        onClick = {
                            navController.navigate(Destinations.ProductScreen.createRoute(stock.symbol))
                        }
                    )
                }
            }
        }
    }
}