package com.ta.prediksireksadanaarima.models

data class MutualFundPriceDecryptedResponseModel(
    val change: Float,
    val percent: Float,
    val chart: List<MutualFundPriceModel2>,
    val xaxisopt: String
)

//data class DecryptedDataResponseModel(
//    val data: MutualFundPriceDecryptedResponseModel
//)
