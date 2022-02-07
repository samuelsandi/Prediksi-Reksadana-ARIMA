package com.ta.prediksireksadanaarima.models

data class MutualFundStringResponseModel(
    val change: Float,
    val percent: Float,
    val chart: List<MutualFundChartResponseModel>,
    val xaxisopt: String
)