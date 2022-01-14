package com.ta.prediksireksadanaarima

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class Chart : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private var fundPriceList = ArrayList<MutualFundPriceModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val intent = intent
        val mutualFundName = intent.getStringExtra("mutualFundName")

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = mutualFundName
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        lineChart = findViewById(R.id.lineChart)
        initLineChart()
        setDataToLineChart()

//        ApiData.apiData( object :ApiData.Response{
//            override fun data(data: ResponseModel.Result, status: Boolean) {
//                if(status){
//                    val items:List<ResponseModel.MutualFundPriceModel> = data.data.chart
//                }
//            }
//
//        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initLineChart() {

        //hide grid lines
        lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        lineChart.axisRight.isEnabled = false

        //remove legend
        lineChart.legend.isEnabled = false


        //remove description label
        lineChart.description.isEnabled = false


        //add animation
        lineChart.animateX(1000, Easing.EaseInSine)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < fundPriceList.size) {
                fundPriceList[index].date
            } else {
                ""
            }
        }
    }

    private fun setDataToLineChart() {
        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()

        fundPriceList = getPriceList()

        //you can replace this data object with  your custom object
        for (i in fundPriceList.indices) {
            val fundPrice = fundPriceList[i]
            entries.add(Entry(i.toFloat(), fundPrice.price))
        }

        val lineDataSet = LineDataSet(entries, "")

        val data = LineData(lineDataSet)
        lineChart.data = data

        lineChart.invalidate()
    }

    // simulate api call
    // we are initialising it directly
    private fun getPriceList(): ArrayList<MutualFundPriceModel> {
        fundPriceList.add(MutualFundPriceModel("2022-01-01", 56000.34f))
        fundPriceList.add(MutualFundPriceModel("2022-01-02", 60000.56f))
        fundPriceList.add(MutualFundPriceModel("2022-01-03", 20000.12f))
        fundPriceList.add(MutualFundPriceModel("2022-01-06", 56000.89f))
        fundPriceList.add(MutualFundPriceModel("2022-01-07", 100000.25f))

        return fundPriceList
    }

}