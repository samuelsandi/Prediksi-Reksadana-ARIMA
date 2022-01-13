package com.ta.prediksireksadanaarima

object ResponseModel {
    data class Result(val data: Data)
    data class Data(val change: Double,
                    val percent: Double,
                    val chart: List<MutualFundPriceModel>,
                    val xaxisopt: String)
    data class MutualFundPriceModel(
        val date:Int,
        val formated_date:String,
        val xlabel:Int,
        val values:Float,
        val value_adjusted:Float)
}