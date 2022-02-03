package com.ta.prediksireksadanaarima.viewModels

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.ta.prediksireksadanaarima.views.MultiLineChartActivity

class MultiLineChartViewModel: ViewModel() {

    fun initChart(activity: MultiLineChartActivity){
        activity.chart.setOnChartValueSelectedListener(activity)
        activity.chart.setDrawGridBackground(false)
        activity.chart.description.isEnabled = false
        activity.chart.setDrawBorders(false)
        activity.chart.axisLeft.isEnabled = false
        activity.chart.axisRight.setDrawAxisLine(false)
        activity.chart.axisRight.setDrawGridLines(false)
        activity.chart.xAxis.setDrawAxisLine(false)
        activity.chart.xAxis.setDrawGridLines(false)

        // to draw label on xAxis (from chart code)
        activity.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        activity.chart.xAxis.valueFormatter = activity.MyAxisFormatter()
        activity.chart.xAxis.setDrawLabels(true)
        activity.chart.xAxis.granularity = 1f
        activity.chart.xAxis.labelRotationAngle = +90f

        // enable touch gestures
        activity.chart.setTouchEnabled(true)

        // enable scaling and dragging
        activity.chart.isDragEnabled = true
        activity.chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        activity.chart.setPinchZoom(false)
        val l = activity.chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
    }
}