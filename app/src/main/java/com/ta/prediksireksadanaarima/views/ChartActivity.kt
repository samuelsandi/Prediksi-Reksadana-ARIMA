package com.ta.prediksireksadanaarima.views

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.TextView
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import java.util.ArrayList
import androidx.appcompat.app.AppCompatActivity
import com.ta.prediksireksadanaarima.PriceEntry
import com.ta.prediksireksadanaarima.utilities.APIHandler
import com.ta.prediksireksadanaarima.R
import kotlin.system.measureTimeMillis

class ChartActivity : OnChartValueSelectedListener, AppCompatActivity() {
    private lateinit var chart: LineChart
    var fundPriceList = ArrayList<PriceEntry>()
    var predictionList = ArrayList<PriceEntry>()
    private var tfRegular: Typeface? = null
    private var tfLight: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val timeInMillis = measureTimeMillis {
            //Start Measuring Time
            super.onCreate(savedInstanceState)
            tfRegular = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")
            tfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")

            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            setContentView(R.layout.activity_multi_line_chart)

            val intent = intent
            val rdCode = intent.getStringExtra("rdCode")
            val rdName = intent.getStringExtra("rdName")

            //actionbar
            val actionbar = supportActionBar
            //set actionbar title
            actionbar!!.title = rdName
            //set back button
            actionbar.setDisplayHomeAsUpEnabled(true)

            chart = findViewById(R.id.chart1)

            val handler = APIHandler()
            handler.getPriceList(rdCode, this)
            initChart()
            //Stop Measuring Time
        }

        Log.d("MVVM-TIME_","(The operation took $timeInMillis ms)")

    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            val fundPriceLists: List<PriceEntry> = fundPriceList + predictionList
            return if (index < fundPriceLists.size) {
                fundPriceLists[index].date
            } else {
                ""
            }
        }
    }

    private fun initChart(){
        chart.setOnChartValueSelectedListener(this)
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

    fun setChartData(){
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.line, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.actionToggleValues -> {
                val sets = chart.data
                    .dataSets
                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.setDrawValues(!set.isDrawValuesEnabled)
                }
                chart.invalidate()
            }
            R.id.actionToggleHighlight -> {
                if (chart.data != null) {
                    chart.data.isHighlightEnabled = !chart.data.isHighlightEnabled
                    chart.invalidate()
                }
            }
            R.id.actionToggleCircles -> {
                val sets = chart.data
                    .dataSets
                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.setDrawCircles(!set.isDrawCirclesEnabled)
                }
                chart.invalidate()
            }
        }
        return true
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i(
            "VAL SELECTED",
            "Value: " + e.y + ", xIndex: " + e.x
                    + ", DataSet index: " + h.dataSetIndex
        )
    }

    override fun onNothingSelected() {}

}