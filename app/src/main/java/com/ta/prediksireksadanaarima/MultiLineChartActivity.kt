package com.ta.prediksireksadanaarima

import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.charts.LineChart
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.github.mikephil.charting.components.Legend
import android.widget.TextView
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.utils.ColorTemplate
import android.view.MotionEvent
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import java.util.ArrayList

class MultiLineChartActivity : DemoBase(), OnChartGestureListener, OnChartValueSelectedListener {
    private lateinit var chart: LineChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_multi_line_chart)

        val intent = intent
        val mutualFundName = intent.getStringExtra("mutualFundName")

        title = "MultiLineChartActivity"
        chart = findViewById(R.id.chart1)
        chart.setOnChartValueSelectedListener(this)
        chart.setDrawGridBackground(false)
        chart.description.isEnabled = false
        chart.setDrawBorders(false)
        chart.axisLeft.isEnabled = false
        chart.axisRight.setDrawAxisLine(false)
        chart.axisRight.setDrawGridLines(false)
        chart.xAxis.setDrawAxisLine(false)
        chart.xAxis.setDrawGridLines(false)

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
        val tvX = findViewById<TextView>(R.id.tvXMax)
        val tvY = findViewById<TextView>(R.id.tvYMax)
        tvX.text = "TBD"
        tvY.text = "TBD"

        // Chart initialization
        chart.resetTracking()
        val dataSets = ArrayList<ILineDataSet>()
        for (z in 0..2) {
            val values = ArrayList<Entry>()
            for (i in 0..99) {
                val `val` = Math.random() * 100 + 3
                values.add(Entry(i.toFloat(), `val`.toFloat()))
            }
            val d = LineDataSet(values, "DataSet " + (z + 1))
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

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = mutualFundName
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    private val colors = intArrayOf(
        ColorTemplate.VORDIPLOM_COLORS[0],
        ColorTemplate.VORDIPLOM_COLORS[1],
        ColorTemplate.VORDIPLOM_COLORS[2]
    )

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.line, menu)
        menu.removeItem(R.id.actionToggleIcons)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
}