package com.ta.prediksireksadanaarima.utilities

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ta.prediksireksadanaarima.models.MutualFundPriceResponseModel
import com.ta.prediksireksadanaarima.views.MultiLineChartActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class APIHandler {

    fun getPriceList(rdCode: String?,
                     chartActivity: MultiLineChartActivity
    ) {

        //API and JSON Handler
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val service = Retrofit.Builder()
            .baseUrl(MutualFundPriceService.API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MutualFundPriceService::class.java)

        service.getMutualFundPrice(rdCode).enqueue(object : Callback<MutualFundPriceResponseModel> {
            override fun onFailure(call: Call<MutualFundPriceResponseModel>, t: Throwable) {
                Log.d("TAG_", "An error happened!")
                t.printStackTrace()
            }
            override fun onResponse(call: Call<MutualFundPriceResponseModel>,
                                    response: Response<MutualFundPriceResponseModel>) {
                /* This will print the response of the network call to the Logcat */
                for (i in response.body()!!.pastPrices.indices){
                    chartActivity.fundPriceList.add(response.body()!!.pastPrices[i])
                }
                for (i in response.body()!!.predictionPrices.indices){
                    chartActivity.predPriceList.add(response.body()!!.predictionPrices[i])
                }
                chartActivity.setChartData()
            }
        })
    }

}