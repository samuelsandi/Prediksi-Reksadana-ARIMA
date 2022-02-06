package com.ta.prediksireksadanaarima.models

data class MutualFundPriceModel (
    val date: String,
    val price: Float
)

data class MutualFundPriceModel2 (
    val date: Float,
    val formated_date: String,
    val xlabel: Float,
    val value: Float,
    val value_adjusted: Float
)