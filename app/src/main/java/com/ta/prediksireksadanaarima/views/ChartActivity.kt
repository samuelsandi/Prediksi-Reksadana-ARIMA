package com.ta.prediksireksadanaarima.views

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import androidx.appcompat.app.AppCompatActivity
import com.ta.prediksireksadanaarima.utilities.APIHandler
import com.ta.prediksireksadanaarima.R
import com.ta.prediksireksadanaarima.viewModels.ChartViewModel

class ChartActivity: OnChartValueSelectedListener, AppCompatActivity() {

    private val viewModel = ChartViewModel()
    private lateinit var chart: LineChart
    private var tfRegular: Typeface? = null
    private var tfLight: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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
        handler.getPriceList(rdCode, viewModel, chart)
        chart.setOnChartValueSelectedListener(this)
        viewModel.initChart(chart)
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