package com.example.growwtask.presentation.ui.productScreen.components

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.growwtask.R
import com.example.growwtask.data.model.StockPriceData
import com.example.growwtask.data.model.StockSymbolDetailsResponse
import androidx.core.net.toUri


@SuppressLint("DefaultLocale")
@Composable
fun StockDetailsContent(
    stockDetails: StockSymbolDetailsResponse,
    priceData: List<StockPriceData>,
    modifier: Modifier = Modifier
) {
    val currentPrice = priceData.firstOrNull()?.close ?: 0f
    val previousPrice = priceData.getOrNull(1)?.close ?: 0f
    val priceChange = currentPrice - previousPrice
    val priceChangePercentage = (priceChange / previousPrice) * 100
    val isPriceUp = priceChange >= 0

    val formattedPrice = "$${String.format("%.2f", currentPrice)}"
    val formattedChange = String.format("%+.2f", priceChange)
    val formattedPercentage = String.format("%+.2f%%", priceChangePercentage)

    val timeOptions = listOf("1D", "1W", "1M", "3M", "1Y")
    var selectedTimeOption by remember { mutableStateOf("1W") }
    val context = LocalContext.current

    val surfaceColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryContainerColor = MaterialTheme.colorScheme.secondaryContainer
    val positiveColor = Color(0xFF4CAF50)
    val negativeColor = Color(0xFFE53935)
    val cardBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val cardContentColor = MaterialTheme.colorScheme.onSurfaceVariant

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor)
            ) {
                // Stock header with logo and basic info
                Surface(
                    color = primaryColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Company logo
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(surfaceColor)
                                .border(2.dp, primaryColor.copy(alpha = 0.5f), CircleShape)
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "Company Logo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        // Company info
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            Text(
                                text = stockDetails.Name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = contentColor
                            )
                            Text(
                                text = "${stockDetails.Symbol}, ${stockDetails.AssetType}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = contentColor.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${stockDetails.Exchange} • ${stockDetails.Currency}",
                                style = MaterialTheme.typography.bodySmall,
                                color = contentColor.copy(alpha = 0.6f)
                            )
                        }

                        // Price info
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = formattedPrice,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = contentColor
                            )
                            Text(
                                text = "$formattedChange ($formattedPercentage)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isPriceUp) positiveColor else negativeColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Chart
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Price display in card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = formattedPrice,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = cardContentColor
                                )
                                Text(
                                    text = "$formattedChange ($formattedPercentage)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isPriceUp) positiveColor else negativeColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (isPriceUp) positiveColor.copy(alpha = 0.1f)
                                else negativeColor.copy(alpha = 0.1f),
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPriceUp) Icons.Default.KeyboardArrowUp
                                    else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (isPriceUp) "Price Up" else "Price Down",
                                    tint = if (isPriceUp) positiveColor else negativeColor,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }

                        // Custom Stock Chart using Canvas
                        StockChart(
                            stockData = priceData,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(vertical = 8.dp),
                            lineColor = if (isPriceUp) positiveColor else negativeColor
                        )

                        // Time period selector
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            timeOptions.forEach { option ->
                                TimeOptionButton(
                                    text = option,
                                    isSelected = option == selectedTimeOption,
                                    selectedColor = primaryColor,
                                    onClick = { selectedTimeOption = option },
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // About section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "About ${stockDetails.Name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = cardContentColor,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = stockDetails.Description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = cardContentColor.copy(alpha = 0.8f),
                            lineHeight = 20.sp,
                            maxLines = 6,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (stockDetails.OfficialSite.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = cardContentColor.copy(alpha = 0.1f))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .clickable {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            stockDetails.OfficialSite.toUri()
                                        )
                                        context.startActivity(intent)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Website",
                                    tint = primaryColor
                                )
                                Text(
                                    text = "Visit Official Website",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = primaryColor,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Industry and sector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = secondaryContainerColor)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Industry",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = stockDetails.Industry,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = secondaryContainerColor)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Sector",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = stockDetails.Sector,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price range
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "52-Week Range",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = cardContentColor,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Low",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = cardContentColor.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "$${stockDetails.`52WeekLow`}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = cardContentColor
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Current",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = cardContentColor.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = formattedPrice,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPriceUp) positiveColor else negativeColor
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "High",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = cardContentColor.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "$${stockDetails.`52WeekHigh`}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = cardContentColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Calculate slider position
                        val low = stockDetails.`52WeekLow`.toFloatOrNull() ?: 0f
                        val high = stockDetails.`52WeekHigh`.toFloatOrNull() ?: 0f
                        val range = high - low
                        val normalizedPrice = if (range > 0f) (currentPrice - low) / range else 0.5f

                        Slider(
                            value = normalizedPrice.coerceIn(0f, 1f),
                            onValueChange = {},
                            enabled = false,
                            colors = SliderDefaults.colors(
                                thumbColor = if (isPriceUp) positiveColor else negativeColor,
                                activeTrackColor = if (isPriceUp) positiveColor else negativeColor,
                                inactiveTrackColor = cardContentColor.copy(alpha = 0.1f)
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Key metrics
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Key Metrics",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = cardContentColor,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            KeyMetric(
                                title = "Market Cap",
                                value = stockDetails.MarketCapitalization,
                                contentColor = cardContentColor
                            )
                            KeyMetric(
                                title = "P/E Ratio",
                                value = stockDetails.PERatio,
                                contentColor = cardContentColor
                            )
                            KeyMetric(
                                title = "Beta",
                                value = stockDetails.Beta,
                                contentColor = cardContentColor
                            )
                        }

                        HorizontalDivider(color = cardContentColor.copy(alpha = 0.1f))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            KeyMetric(
                                title = "Dividend Yield",
                                value = stockDetails.DividendYield,
                                contentColor = cardContentColor
                            )
                            KeyMetric(
                                title = "EPS",
                                value = stockDetails.EPS,
                                contentColor = cardContentColor
                            )
                            KeyMetric(
                                title = "Profit Margin",
                                value = stockDetails.ProfitMargin,
                                contentColor = cardContentColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Moving Averages
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Moving Averages",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = cardContentColor,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = contentColor.copy(alpha = 0.05f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "50-Day MA",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = contentColor.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = "$${stockDetails.`50DayMovingAverage`}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = contentColor
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = contentColor.copy(alpha = 0.05f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "200-Day MA",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = contentColor.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = "$${stockDetails.`200DayMovingAverage`}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = contentColor
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
