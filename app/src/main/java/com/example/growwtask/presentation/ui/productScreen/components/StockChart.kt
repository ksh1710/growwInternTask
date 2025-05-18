package com.example.growwtask.presentation.ui.productScreen.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.growwtask.data.model.StockPriceData
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun StockChart(
    stockData: List<StockPriceData>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF4285F4),
    showGrid: Boolean = true
) {
    if (stockData.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40f

        val chartWidth = width - 2 * padding
        val chartHeight = height - 2 * padding

        val closePrices = stockData.map { it.close }.reversed()

        val minPrice = closePrices.minOrNull() ?: 0f
        val maxPrice = closePrices.maxOrNull() ?: 0f
        val priceRange = maxPrice - minPrice

        if (showGrid) {
            val ySteps = 4
            val yStepSize = chartHeight / ySteps
            val priceStepSize = priceRange / ySteps

            for (i in 0..ySteps) {
                val y = height - padding - (i * yStepSize)

                drawLine(
                    color = Color.LightGray,
                    start = Offset(padding, y),
                    end = Offset(width - padding, y),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                )

                val price = minPrice + (i * priceStepSize)
                val formattedPrice = "$${String.format("%.2f", price)}"
                drawText(
                    textMeasurer = textMeasurer,
                    text = formattedPrice,
                    topLeft = Offset(5f, y - 10),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                )
            }
        }

        val dates = stockData.map { it.date }.reversed()
        val xSteps = minOf(5, dates.size - 1)
        val xStepSize = chartWidth / xSteps

        for (i in 0..xSteps) {
            if (i < dates.size) {
                val x = padding + (i * xStepSize)
                val dateIndex = (i * (dates.size - 1) / xSteps).coerceIn(0, dates.size - 1)
                val date = formatDate(dates[dateIndex])

                drawText(
                    textMeasurer = textMeasurer,
                    text = date,
                    topLeft = Offset(x - 25, height - 15),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 8.sp
                    )
                )
            }
        }

        val path = Path()
        val points = closePrices.mapIndexed { index, price ->
            val x = padding + (index.toFloat() / (closePrices.size - 1)) * chartWidth
            val normalizedPrice = if (priceRange > 0) (price - minPrice) / priceRange else 0.5f
            val y = height - padding - (normalizedPrice * chartHeight)
            Offset(x, y)
        }

        if (points.isNotEmpty()) {
            path.moveTo(points.first().x, points.first().y)

            for (i in 1 until points.size) {
                path.lineTo(points[i].x, points[i].y)
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(
                    width = 2.5f
                )
            )
            
            val fillPath = Path().apply {
                addPath(path)
                lineTo(points.last().x, height - padding)
                lineTo(points.first().x, height - padding)
                close()
            }
            
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.3f),
                        lineColor.copy(alpha = 0.0f)
                    ),
                    startY = points.minOf { it.y },
                    endY = height - padding
                )
            )
        }
    }
}

fun formatDate(dateString: String): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputFormat = SimpleDateFormat("MM/dd", Locale.US)
        val date = inputFormat.parse(dateString)
        return date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        Log.d("idk", "formatDate: $e")
        return dateString
    }

}