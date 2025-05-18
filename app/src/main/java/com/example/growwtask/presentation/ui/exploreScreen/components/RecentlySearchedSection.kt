package com.example.growwtask.presentation.ui.exploreScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.growwtask.data.model.RecentlySearchedStock

@Composable
fun RecentlySearchedSection(
    recentStocks: List<RecentlySearchedStock>,
    onStockClick: (String) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ExploreHeadingRow(
            title = "Recently Searched",
            onViewAllClick = onViewAllClick
        )
        
        if (recentStocks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recently searched stocks",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        } else {
            recentStocks.take(3).forEach { stock ->
                RecentlySearchedItem(
                    stock = stock,
                    onClick = { onStockClick(stock.symbol) }
                )
            }
        }
    }
}

@Composable
fun RecentlySearchedItem(
    stock: RecentlySearchedStock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stock.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Thin
        )
        
        Text(
            text = stock.price,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = Color.LightGray,
        thickness = 1.dp
    )
}