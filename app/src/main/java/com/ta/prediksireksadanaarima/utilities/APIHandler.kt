package com.ta.prediksireksadanaarima.utilities

import android.util.Log
import com.github.mikephil.charting.charts.LineChart
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ta.prediksireksadanaarima.models.MutualFundStringResponseModel
import com.ta.prediksireksadanaarima.models.MutualFundPriceModel
import com.ta.prediksireksadanaarima.models.MutualFundRawResponseModel
import com.ta.prediksireksadanaarima.viewModels.ChartViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class APIHandler {

    fun getPriceList(rdCode: String?,
                     viewModel: ChartViewModel,
                     chart: LineChart) {

        //API and JSON Handler
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val service = Retrofit.Builder()
            .baseUrl(MutualFundPriceService.API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MutualFundPriceService::class.java)

        service.getMutualFundPrice(rdCode).enqueue(object : Callback<MutualFundRawResponseModel> {
            override fun onFailure(call: Call<MutualFundRawResponseModel>, t: Throwable) {
                Log.d("TAG_", "An error happened!")
                t.printStackTrace()
            }
            override fun onResponse(call: Call<MutualFundRawResponseModel>,
                                    response: Response<MutualFundRawResponseModel>) {
//                for (i in response.body()!!.pastPrices.indices){
//                    viewModel.fundPriceList.add(response.body()!!.pastPrices[i])
//                }
//                for (i in response.body()!!.predictionPrices.indices){
//                    viewModel.predictionList.add(response.body()!!.predictionPrices[i])
//                }
                val decryptedString = AESDecryptor(response.body()!!.data).decrypt()
                val adapter = moshi.adapter(MutualFundStringResponseModel::class.java)
                val data = adapter.fromJson(decryptedString)
                val chartModel = data!!.chart

                for (i in chartModel.indices) {
                    val priceModel = MutualFundPriceModel(chartModel[i].formated_date,
                                                            chartModel[i].value)
                    viewModel.fundPriceList.add(priceModel)
                }

                viewModel.predictionList = Predictor(viewModel.fundPriceList).predict()

                viewModel.setChartData(chart)
            }
        })
    }

}