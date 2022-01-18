package com.ta.prediksireksadanaarima.models

data class MutualFundPriceResponseModel(
    val pastPrices: List<MutualFundPriceModel>,
    val predictionPrices: List<MutualFundPriceModel>
)