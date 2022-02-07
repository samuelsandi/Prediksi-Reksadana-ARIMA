package com.ta.prediksireksadanaarima.models

data class MutualFundChartResponseModel (
    val date: Float,
    val formated_date: String,
    val xlabel: Float,
    val value: Float,
    val value_adjusted: Float
)