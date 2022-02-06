package com.ta.prediksireksadanaarima

data class MutualFundPriceResponse(
    val pastPrices: List<PriceEntry>,
    val predictionPrices: List<PriceEntry>
)

data class PriceEntry (
    val date:String,
    val price:Float
)