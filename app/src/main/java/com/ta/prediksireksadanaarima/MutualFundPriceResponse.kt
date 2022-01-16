package com.ta.prediksireksadanaarima

data class MutualFundPriceResponse(
    val pric: List<MutualFundPriceModel>,
    val pred: List<MutualFundPriceModel>
)