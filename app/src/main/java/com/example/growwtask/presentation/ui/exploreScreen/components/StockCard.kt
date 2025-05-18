package com.example.growwtask.presentation.ui.exploreScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.growwtask.data.model.StockItem

@Composable
fun StockCard(stock: StockItem, onCardClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onCardClick()
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                modifier = Modifier.clip(CircleShape).size(40.dp),
                painter = painterResource(stock.icon),
                contentDescription = null
            )
            Text(stock.name)
            Text(stock.price)
        }
    }
}
