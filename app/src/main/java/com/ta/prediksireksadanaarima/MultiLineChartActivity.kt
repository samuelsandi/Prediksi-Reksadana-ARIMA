package com.ta.prediksireksadanaarima

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.MotionEvent
import android.widget.TextView
import com.github.mikephil.charting.listener.OnChartGestureListener
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
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.ArrayList

class MultiLineChartActivity : DemoBase(), OnChartGestureListener, OnChartValueSelectedListener {
    private lateinit var chart: LineChart
    private var fundPriceLists = ArrayList<ArrayList<MutualFundPriceModel>>()
    private lateinit var tvX: TextView
    private lateinit var tvY: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_multi_line_chart)

        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)

        val intent = intent
        val mutualFundName = intent.getStringExtra("mutualFundName")

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = mutualFundName
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        chart = findViewById(R.id.chart1)
        getPriceList()
        initChart()
        setChartData()
    }

    private val colors = intArrayOf(
        ColorTemplate.VORDIPLOM_COLORS[0],
        ColorTemplate.VORDIPLOM_COLORS[1]
    )

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < fundPriceLists[1].size) {
                fundPriceLists[1][index].date
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
        tvX.text = "TBD"
        tvY.text = "TBD"
    }

    private fun setChartData(){
        //SetChartData
        chart.resetTracking()
        val dataSets = ArrayList<ILineDataSet>()
        for (z in fundPriceLists.indices) {
            val entries = ArrayList<Entry>()
            for (i in fundPriceLists[z].indices) {
                val fundPrice = fundPriceLists[z][i]
                entries.add(Entry(i.toFloat(), fundPrice.price))
            }
            val d = LineDataSet(entries, "DataSet " + (z + 1))
            d.lineWidth = 2.5f
            d.circleRadius = 4f
            val color = colors[z % colors.size]
            d.color = color
            d.setCircleColor(color)
            dataSets.add(d)
        }
        val data = LineData(dataSets)
        chart.data = data
        chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.line, menu)
        menu.removeItem(R.id.actionToggleIcons)
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
            R.id.actionToggleAutoScaleMinMax -> {
                chart.isAutoScaleMinMaxEnabled = !chart.isAutoScaleMinMaxEnabled
                chart.notifyDataSetChanged()
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

    override fun onChartGestureStart(me: MotionEvent, lastPerformedGesture: ChartGesture) {
        Log.i("Gesture", "START, x: " + me.x + ", y: " + me.y)
    }

    override fun onChartGestureEnd(me: MotionEvent, lastPerformedGesture: ChartGesture) {
        Log.i("Gesture", "END, lastGesture: $lastPerformedGesture")

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartGesture.SINGLE_TAP) chart.highlightValues(null) // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    override fun onChartLongPressed(me: MotionEvent) {
        Log.i("LongPress", "Chart long pressed.")
    }

    override fun onChartDoubleTapped(me: MotionEvent) {
        Log.i("DoubleTap", "Chart double-tapped.")
    }

    override fun onChartSingleTapped(me: MotionEvent) {
        Log.i("SingleTap", "Chart single-tapped.")
    }

    override fun onChartFling(
        me1: MotionEvent,
        me2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ) {
        Log.i("Fling", "Chart fling. VelocityX: $velocityX, VelocityY: $velocityY")
    }

    override fun onChartScale(me: MotionEvent, scaleX: Float, scaleY: Float) {
        Log.i("Scale / Zoom", "ScaleX: $scaleX, ScaleY: $scaleY")
    }

    override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {
        Log.i("Translate / Move", "dX: $dX, dY: $dY")
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i(
            "VAL SELECTED",
            "Value: " + e.y + ", xIndex: " + e.x
                    + ", DataSet index: " + h.dataSetIndex
        )
    }

    override fun onNothingSelected() {}

    // simulate api call
    // we are initialising it directly
    private fun getPriceList() {
        val fPriceTemp = ArrayList<MutualFundPriceModel>()
        val fPriceTemp2 = ArrayList<MutualFundPriceModel>()

        fPriceTemp.add(MutualFundPriceModel("2022-01-01", 56000.34f))
        fPriceTemp.add(MutualFundPriceModel("2022-01-02", 60000.56f))
        fPriceTemp.add(MutualFundPriceModel("2022-01-03", 20000.12f))
        fPriceTemp.add(MutualFundPriceModel("2022-01-06", 56000.89f))
        fPriceTemp.add(MutualFundPriceModel("2022-01-07", 1000000f))

        fPriceTemp2.add(MutualFundPriceModel("2022-01-01", 70000.34f))
        fPriceTemp2.add(MutualFundPriceModel("2022-01-02", 40000.5f))
        fPriceTemp2.add(MutualFundPriceModel("2022-01-03", 30000f))
        fPriceTemp2.add(MutualFundPriceModel("2022-01-06", 2000f))
        fPriceTemp2.add(MutualFundPriceModel("2022-01-07", 90000f))

        fundPriceLists.add(fPriceTemp)
        fundPriceLists.add(fPriceTemp2)

        //API and JSON Handler
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        /* Creates an instance of the UserService using a simple Retrofit builder using Moshi
         * as a JSON converter, this will append the endpoints set on the UserService interface
         * (for example '/api', '/api?results=2') with the base URL set here, resulting on the
         * full URL that will be called: 'https://randomuser.me/api' */
        val service = Retrofit.Builder()
            .baseUrl("https://arima-reksadana-api.vercel.app")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MutualFundPriceService::class.java)

        /* Calls the endpoint set on getUsers (/api) from UserService using enqueue method
         * that creates a new worker thread to make the HTTP call */
        service.getMutualFundPrice().enqueue(object : Callback<MutualFundPriceResponse> {

            /* The HTTP call failed. This method is run on the main thread */
            override fun onFailure(call: Call<MutualFundPriceResponse>, t: Throwable) {
                Log.d("TAG_", "An error happened!")
                t.printStackTrace()
            }

            /* The HTTP call was successful, we should still check status code and response body
             * on a production app. This method is run on the main thread */
            override fun onResponse(call: Call<MutualFundPriceResponse>, response: Response<MutualFundPriceResponse>) {
                /* This will print the response of the network call to the Logcat */
                Log.d("TAG_", moshi.adapter(MutualFundPriceResponse::class.java).toJson(response.body()))
            }
        })
    }
}