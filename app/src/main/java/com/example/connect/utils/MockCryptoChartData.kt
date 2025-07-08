package com.example.connect.utils

import com.github.mikephil.charting.data.Entry

object MockCryptoChartData {
    fun getPriceData(timeFrame: String): List<Entry> {
        return when (timeFrame) {
            "1H" -> listOf(Entry(0f, 78000f), Entry(1f, 78500f), Entry(2f, 78400f), Entry(3f, 79000f))
            "1D" -> listOf(Entry(0f, 76000f), Entry(1f, 77000f), Entry(2f, 77500f), Entry(3f, 79400f), Entry(4f, 79200f))
            "1W" -> listOf(Entry(0f, 74000f), Entry(1f, 75000f), Entry(2f, 76500f), Entry(3f, 78200f), Entry(4f, 79400f))
            "1M" -> List(30) { Entry(it.toFloat(), (72000 + it * 300).toFloat()) }
            "1Y" -> List(12) { Entry(it.toFloat(), (50000 + it * 2500).toFloat()) }
            "All" -> List(60) { Entry(it.toFloat(), (20000 + it * 1000).toFloat()) }
            else -> emptyList()
        }
    }
}
