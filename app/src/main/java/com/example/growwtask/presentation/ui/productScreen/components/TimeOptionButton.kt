package com.example.growwtask.presentation.ui.productScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TimeOptionButton(
    text: String,
    isSelected: Boolean,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = if (isSelected) selectedColor.copy(alpha = 0.1f) else Color.Transparent,
            border = BorderStroke(
                width = 1.dp,
                color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            ),
            modifier = modifier.clickable { onClick() }
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
