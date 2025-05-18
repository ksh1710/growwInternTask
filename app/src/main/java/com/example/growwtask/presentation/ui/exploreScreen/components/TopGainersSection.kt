package com.example.growwtask.presentation.ui.exploreScreen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.growwtask.R
import com.example.growwtask.data.model.StockItem
import com.example.growwtask.data.model.TopGainer
import com.example.growwtask.presentation.common.EmptyState
import com.example.growwtask.presentation.navigation.Destinations


@Composable
fun TopGainersSection(
    onViewAllClick: () -> Unit,
    topGainers: List<TopGainer> = emptyList(),
    navController: NavController,
    isLoading: Boolean
) {

    ExploreHeadingRow(title = "Top Gainers", onViewAllClick = { onViewAllClick() })

    if (isLoading) {
        CircularProgressIndicator()
    } else if (topGainers.isEmpty()) {
        EmptyState(message = "No data available")
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(300.dp),
            userScrollEnabled = false,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(minOf(topGainers.size, 4)) { index ->
                val item = topGainers[index]
                StockCard(
                    stock = StockItem(
                        name = item.ticker,
                        price = item.price,
                        icon = R.drawable.ic_launcher_foreground
                    ),
                    onCardClick = {
                        navController.navigate(Destinations.ProductScreen.createRoute(item.ticker))
                    }
                )
            }
        }
    }
}
