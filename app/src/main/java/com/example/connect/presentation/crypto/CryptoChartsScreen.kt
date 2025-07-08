package com.example.connect.presentation.crypto

import android.widget.FrameLayout
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.connect.data.model.CandleData
import com.example.connect.presentation.qrscanner.BottomNavigationBarWithCenterChat
import com.example.connect.utils.MockCryptoChartData
import com.example.connect.utils.Route
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.abs
import kotlin.math.min
import android.graphics.Color as GColor

@Composable
fun CryptoChartsScreen(modifier: Modifier = Modifier) {
    val timeFrames = listOf("1H", "1D", "1W", "1M", "1Y", "All")
    var selectedTimeFrame by remember { mutableStateOf("1D") }
    val priceData = remember(selectedTimeFrame) {
        MockCryptoChartData.getPriceData(selectedTimeFrame)
    }



    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier.height(16.dp))
            Text(
                text = "PRICE ALERTS",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Bitcoin", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(
                        text = "\u00A379,440.60",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "\u00A379,321.46 (59.0%)",
                        color = Color.Green,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LineChartView(entries = priceData)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        timeFrames.forEach { frame ->
                            Text(
                                text = frame,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .selectable(
                                        selected = selectedTimeFrame == frame,
                                        onClick = { selectedTimeFrame = frame }
                                    ),
                                fontWeight = if (selectedTimeFrame == frame) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(35.dp))

                    CandlestickChartWithVolume(sampleCandles, modifier)
                }

            }
        }
    }
}

@Composable
fun LineChartView(entries: List<Entry>) {
    val context = LocalContext.current

    fun setupStyledChart(chart: LineChart, entries: List<Entry>) {
        val dataSet = LineDataSet(entries, "").apply {
            color = Color.Red.toArgb()                // Line color
            fillColor = Color.Red.toArgb()           // Fill under line
            setDrawFilled(true)             // Enable fill
            setDrawCircles(false)           // Remove circle points
            setDrawValues(false)            // Hide point values
            lineWidth = 2f
            mode = LineDataSet.Mode.CUBIC_BEZIER // Optional for smooth curves
        }

        chart.apply {
            data = LineData(dataSet)

            setTouchEnabled(false)
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawGridBackground(false)
            setViewPortOffsets(0f, 0f, 0f, 0f) // tight layout

            setBackgroundColor(0xf0000) // dark background

            description.isEnabled = false
            legend.isEnabled = false

            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            xAxis.isEnabled = false

            invalidate()
        }
    }

    val entries1 = listOf(
        Entry(0f, 100f),
        Entry(1f, 95f),
        Entry(2f, 97f),
        Entry(3f, 94f),
        Entry(4f, 90f),
        Entry(5f, 85f),
    )




    AndroidView(factory = {
        LineChart(context).apply {
            setupStyledChart(chart = this, entries1)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                400
            )

            val dataSet = LineDataSet(entries, "Bitcoin Price").apply {
                color = GColor.rgb(255, 140, 0)
                valueTextColor = GColor.BLACK
                valueTextSize = 10f
                lineWidth = 2f
                circleRadius = 4f
                setCircleColor(GColor.rgb(255, 140, 0))
                setDrawFilled(true)
                fillColor = GColor.rgb(255, 204, 128)
            }

            val lineData = LineData(dataSet)
            this.data = lineData

            description = Description().apply { text = "" }
            invalidate()
        }
    })
}

@Composable
fun CandlestickChartWithVolume(
    candleList: List<CandleData>,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .height(300.dp)
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        val candleWidth = 20f
        val spacing = 30f
        val maxPrice = candleList.maxOf { it.high }
        val minPrice = candleList.minOf { it.low }
        val maxVolume = candleList.maxOf { it.volume }

        val canvasWidth = size.width
        val canvasHeight = size.height

        val candleChartHeight = canvasHeight * 0.7f
        val volumeChartHeight = canvasHeight * 0.3f

        candleList.forEachIndexed { index, candle ->
            val x = index * spacing + candleWidth
            val scaleY = candleChartHeight / (maxPrice - minPrice)

            val highY = candleChartHeight - (candle.high - minPrice) * scaleY
            val lowY = candleChartHeight - (candle.low - minPrice) * scaleY
            val openY = candleChartHeight - (candle.open - minPrice) * scaleY
            val closeY = candleChartHeight - (candle.close - minPrice) * scaleY

            val candleColor = if (candle.close >= candle.open) Color.Green else Color.Red

            // Wick
            drawLine(
                color = candleColor,
                start = Offset(x, highY),
                end = Offset(x, lowY),
                strokeWidth = 4f
            )

            // Candle body
            drawRect(
                color = candleColor,
                topLeft = Offset(x - candleWidth / 2, min(openY, closeY)),
                size = Size(
                    width = candleWidth,
                    height = abs(openY - closeY)
                )
            )

            // Volume bar
            val volumeBarHeight = (candle.volume / maxVolume) * volumeChartHeight
            drawRect(
                color = Color.Gray,
                topLeft = Offset(x - candleWidth / 2, candleChartHeight + (volumeChartHeight - volumeBarHeight)),
                size = Size(
                    width = candleWidth,
                    height = volumeBarHeight
                )
            )
        }
    }
}


val sampleCandles = listOf(
    CandleData(80000f, 79000f, 82000f, 78000f, 50000f),
    CandleData(79000f, 79500f, 80000f, 78500f, 62000f),
    CandleData(79500f, 77000f, 80500f, 76500f, 45000f),
    CandleData(77000f, 78500f, 79000f, 76500f, 80000f),
    CandleData(78500f, 81000f, 82000f, 78000f, 70000f),
    CandleData(81000f, 80000f, 81500f, 79000f, 60000f),
    CandleData(80000f, 83000f, 84000f, 79500f, 100000f)
)

@Preview(showBackground = true)
@Composable
fun CandlestickChartPreview() {
    CandlestickChartWithVolume(candleList = sampleCandles)
}

