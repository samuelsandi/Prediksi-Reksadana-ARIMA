package com.ta.prediksireksadanaarima

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
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ta.prediksireksadanaarima.models.MutualFundPriceModel
import com.ta.prediksireksadanaarima.models.MutualFundPriceResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.ArrayList

class MultiLineChartActivity : DemoBase(), OnChartValueSelectedListener {
    private lateinit var chart: LineChart
    private var fundPriceList = ArrayList<MutualFundPriceModel>()
    private var predPriceList = ArrayList<MutualFundPriceModel>()
    private lateinit var rdNameTx: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_multi_line_chart)

        rdNameTx = findViewById(R.id.rdNameTx)

        val intent = intent
        val rdCode = intent.getStringExtra("rdCode")
        val rdName = intent.getStringExtra("rdName")

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = rdName
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        rdNameTx.text = rdCode

        chart = findViewById(R.id.chart1)
        getPriceList(rdCode)
        initChart()
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            val fundPriceLists: List<MutualFundPriceModel> = fundPriceList + predPriceList
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

    private fun setChartData(){
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
        for (i in predPriceList.indices){
            val fundPrice = predPriceList[i]
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

    private fun getPriceList(rdCode: String?) {
        //API and JSON Handler
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val service = Retrofit.Builder()
            .baseUrl(MutualFundPriceService.API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MutualFundPriceService::class.java)

        service.getMutualFundPrice(rdCode).enqueue(object : Callback<MutualFundPriceResponseModel> {
            override fun onFailure(call: Call<MutualFundPriceResponseModel>, t: Throwable) {
                Log.d("TAG_", "An error happened!")
                t.printStackTrace()
            }
            override fun onResponse(call: Call<MutualFundPriceResponseModel>, response: Response<MutualFundPriceResponseModel>) {
                /* This will print the response of the network call to the Logcat */
                for (i in response.body()!!.pastPrices.indices){
                    fundPriceList.add(response.body()!!.pastPrices[i])
                }
                for (i in response.body()!!.predictionPrices.indices){
                    predPriceList.add(response.body()!!.predictionPrices[i])
                }
                setChartData()
            }
        })
    }
}