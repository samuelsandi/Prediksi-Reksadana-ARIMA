package com.ta.prediksireksadanaarima.viewModels

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.ta.prediksireksadanaarima.models.MutualFundPriceModel
import java.util.ArrayList

class ChartViewModel: ViewModel() {

    var fundPriceList = ArrayList<MutualFundPriceModel>()
    var predictionList = ArrayList<MutualFundPriceModel>()

    fun initChart(chart: LineChart){
        chart.setDrawGridBackground(false)
        chart.description.isEnabled = false
        chart.setDrawBorders(false)
        chart.axisLeft.isEnabled = false
        chart.axisRight.setDrawAxisLine(false)
        chart.axisRight.setDrawGridLines(false)
        chart.xAxis.setDrawAxisLine(false)
        chart.xAxis.setDrawGridLines(false)

        // to draw label on xAxis (from chart code)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        chart.xAxis.valueFormatter = MyAxisFormatter()
        chart.xAxis.setDrawLabels(true)
        chart.xAxis.granularity = 1f
        chart.xAxis.labelRotationAngle = +90f

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)
        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            val fundPriceLists: List<MutualFundPriceModel> = fundPriceList + predictionList
            return if (index < fundPriceLists.size) {
                fundPriceLists[index].date
            } else {
                ""
            }
        }
    }

    fun setChartData(chart: LineChart){
        chart.resetTracking()
        val dataSets = ArrayList<ILineDataSet>()

        // Prices
        val entries = ArrayList<Entry>()
        for (i in fundPriceList.indices){
            val fundPrice = fundPriceList[i]
            entries.add(Entry(i.toFloat(), fundPrice.price))
        }

        // Prediction prices
        val entries2 = ArrayList<Entry>()
        entries2.add(Entry((fundPriceList.size-1).toFloat(), fundPriceList[fundPriceList.size-1].price))
        for (i in predictionList.indices){
            val fundPrice = predictionList[i]
            entries2.add(Entry(i.toFloat() + entries.size, fundPrice.price))
        }

        // Prediction chart styling
        var d = LineDataSet(entries2, "Prediksi ARIMA")
        d.lineWidth = 2.5f
        d.circleRadius = 4f
        d.color = ColorTemplate.VORDIPLOM_COLORS[0]
        d.setCircleColor(d.color)
        dataSets.add(d)

        // Price chart styling
        d = LineDataSet(entries, "NAB (Rupiah)")
        d.lineWidth = 2.5f
        d.circleRadius = 4f
        d.color = ColorTemplate.VORDIPLOM_COLORS[3]
        d.setCircleColor(d.color)
        dataSets.add(d)

        val data = LineData(dataSets)
        chart.data = data
        chart.invalidate()
    }
}